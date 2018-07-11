package org.openbooth.dao.impl;

import org.openbooth.util.QueryBuilder;
import org.openbooth.util.dbhandler.DBHandler;
import org.openbooth.util.exceptions.DatabaseException;
import org.openbooth.dao.BackgroundCategoryDAO;
import org.openbooth.dao.BackgroundDAO;
import org.openbooth.dao.exceptions.PersistenceException;
import org.openbooth.entities.Background;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * BackgroundDAO
 */
@Repository
public class JDBCBackgroundDAO implements BackgroundDAO{

    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCBackgroundDAO.class);

    public static final String TABLE_NAME = "backgrounds";
    public static final String NAME_COLUMN = "name";
    public static final String ID_COLUMN = "backgroundID";
    public static final String PATH_COLUMN = "path";
    public static final String DELETED_COLUMN = "isDeleted";
    public static final String CATEGORY_COLUMN = "backgroundcategoryID";

    private static final String CONSISTENCY_ERROR_MESSAGE = "The consistency of the database is broken! Multiple rows have been affected.";

    private Connection con;
    private BackgroundCategoryDAO backgroundCategoryDAO;

    @Autowired
    public JDBCBackgroundDAO(DBHandler handler, BackgroundCategoryDAO backgroundCategoryDAO) throws PersistenceException {
        try {
            con = handler.getConnection();
        } catch (DatabaseException e) {
            throw new PersistenceException(e);
        }
        this.backgroundCategoryDAO = backgroundCategoryDAO;
    }

    private static final String CREATE_STATEMENT =
            QueryBuilder.buildInsert(TABLE_NAME, new String[]{NAME_COLUMN,PATH_COLUMN});

    @Override
    public Background create(Background background) throws PersistenceException {
        if(background == null) throw new IllegalArgumentException("Background is null");


        try (PreparedStatement stmt = con.prepareStatement(CREATE_STATEMENT, Statement.RETURN_GENERATED_KEYS)){
            stmt.setString(1,background.getName());
            stmt.setString(2,background.getPath());

            stmt.executeUpdate();
            try(ResultSet rs = stmt.getGeneratedKeys()) {
            rs.next();
            int generatedID = rs.getInt(1);


            Background persistedBackground = new Background(generatedID, background.getName(), background.getPath(), background.getCategory(), background.isDeleted());
            LOGGER.trace("Background {} successfully stored in database", persistedBackground);
            setCategoryForBackground(persistedBackground, background.getCategory());
            return persistedBackground;
            }
        }
        catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    private static final String SET_CATEGORY_STATEMENT =
            QueryBuilder.buildUpdate(TABLE_NAME, CATEGORY_COLUMN, ID_COLUMN);

    private void setCategoryForBackground(Background background, Background.Category category) throws PersistenceException{
        try(PreparedStatement pstmt = con.prepareStatement(SET_CATEGORY_STATEMENT)){
            pstmt.setInt(1, category.getId());
            pstmt.setInt(2, background.getId());
            int updateCount = pstmt.executeUpdate();

            if(updateCount == 1){
                LOGGER.trace("Relation between background {} and category {} successfully presisted", background, category);
            } else if (updateCount == 0){
                throw new PersistenceException("The given relation could not be persisted, because no background with the given id was found");
            } else {
                throw new PersistenceException(CONSISTENCY_ERROR_MESSAGE);
            }
        } catch (SQLException e){
            throw new PersistenceException(e);
        }
    }

    private static final String UPDATE_STATEMENT =
            QueryBuilder.buildUpdate(TABLE_NAME, new String[]{NAME_COLUMN,PATH_COLUMN}, ID_COLUMN);

    @Override
    public void update(Background background) throws PersistenceException {
        if(background==null) throw new IllegalArgumentException("Background is null.");


        try (PreparedStatement stmt = con.prepareStatement(UPDATE_STATEMENT)){
            stmt.setString(1,background.getName());
            stmt.setString(2,background.getPath());
            stmt.setInt(3,background.getId());
            stmt.executeUpdate();
            int returnUpdateCount = stmt.executeUpdate();

            if (returnUpdateCount == 1){
                LOGGER.trace("Background {} has been successfully update in database", background);
            }
            else if (returnUpdateCount == 0) {
                throw new PersistenceException("The given background could not be updated because no background with the given id was found in the database");
            } else {
                throw new PersistenceException(CONSISTENCY_ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    private static final String READ_ONE_STATEMENT =
            QueryBuilder.buildSelectAllColumns(TABLE_NAME, ID_COLUMN);

    @Override
    public Background read(int id) throws PersistenceException {
        try (PreparedStatement stmt = con.prepareStatement(READ_ONE_STATEMENT)){
            stmt.setInt(1, id);
            try(ResultSet rs = stmt.executeQuery()) {
                rs.next();
                Background background = new Background(
                        rs.getInt(ID_COLUMN),
                        rs.getString(NAME_COLUMN),
                        rs.getString(PATH_COLUMN),
                        backgroundCategoryDAO.read(rs.getInt(CATEGORY_COLUMN)),
                        rs.getBoolean(DELETED_COLUMN)
                );
                if (background.getPath() == null) {
                    background.setPath("");
                }
                LOGGER.trace("Background {} successfully read from database", background);
                return background;
            }
        }
        catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    private static final String READ_ALL_STATEMENT =
            QueryBuilder.buildSelectAllColumns(TABLE_NAME, DELETED_COLUMN);

    @Override
    public List<Background> readAll() throws PersistenceException {

        try (PreparedStatement stmt = con.prepareStatement(READ_ALL_STATEMENT)){
            stmt.setBoolean(1, false);
             try(ResultSet rs = stmt.executeQuery()) {

                 List<Background> returnList = new ArrayList<>();

                 while (rs.next()) {
                     Background background = new Background(
                             rs.getInt(ID_COLUMN),
                             rs.getString(NAME_COLUMN),
                             rs.getString(PATH_COLUMN),
                             backgroundCategoryDAO.read(rs.getInt(CATEGORY_COLUMN)),
                             rs.getBoolean(DELETED_COLUMN)
                     );
                     returnList.add(background);
                 }
                 LOGGER.trace("All backgrounds have been read from database: {}", returnList);
                 return returnList;
             }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }


    private static final String READ_ALL_WITH_CATEGORY_STATEMENT =
            QueryBuilder.buildSelectAllColumns(TABLE_NAME, new String[]{CATEGORY_COLUMN, DELETED_COLUMN});

    @Override
    public List<Background> readAllWithCategory(Background.Category category) throws PersistenceException {
        if(category == null) throw new IllegalArgumentException("Category is null");

        try(PreparedStatement stmt = con.prepareStatement(READ_ALL_WITH_CATEGORY_STATEMENT)) {
            stmt.setInt(1, category.getId());
            stmt.setBoolean(2, false);
            try(ResultSet rs = stmt.executeQuery()) {
                List<Background> returnList = new ArrayList<>();

                while (rs.next()) {
                    Background background = new Background(
                            rs.getInt(ID_COLUMN),
                            rs.getString(NAME_COLUMN),
                            rs.getString(PATH_COLUMN),
                            new Background.Category(category.getId(), category.getName(), category.isDeleted()),
                            rs.getBoolean(DELETED_COLUMN)
                    );
                    returnList.add(background);
                }

                LOGGER.trace("All backgrounds with category {} have been read from database: {}", category, returnList);
                return returnList;
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    private static final String DELETE_STATEMENT =
            QueryBuilder.buildUpdate(TABLE_NAME, DELETED_COLUMN, new String[]{ID_COLUMN, DELETED_COLUMN});

    @Override
    public void delete(Background background) throws PersistenceException {

        if (background==null) throw new IllegalArgumentException("background is null");

        try (PreparedStatement stmt = con.prepareStatement(DELETE_STATEMENT)){
            stmt.setBoolean(1, true);
            stmt.setInt(2,background.getId());
            stmt.setBoolean(3, false);
            int returnUpdateCount  = stmt.executeUpdate();

            // Check, if row has been deleted and return suitable boolean value
            if (returnUpdateCount == 1){
                LOGGER.trace("Background {} successfully deleted from database", background);
            }
            else if (returnUpdateCount == 0){
                throw new PersistenceException("The given background could not be deleted because no background with the given id was found in the database");
            }
            else {
                throw new PersistenceException(CONSISTENCY_ERROR_MESSAGE);
            }
        }
        catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }
}
