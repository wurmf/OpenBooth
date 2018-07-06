package org.openbooth.dao.impl;

import org.openbooth.util.QueryBuilder;
import org.openbooth.util.dbhandler.DBHandler;
import org.openbooth.util.exceptions.DatabaseException;
import org.openbooth.dao.BackgroundCategoryDAO;
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
 * H2 database-Specific BackgroundCategoryDAO Implementation
 */
@Repository
public class JDBCBackgroundCategoryDAO implements BackgroundCategoryDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCBackgroundCategoryDAO.class);
    private Connection con;

    private static final String TABLE_NAME = "backgroundcategories";
    private static final String ID_COLUMN = "backgroundcategoryID";
    private static final String NAME_COLUMN = "name";
    private static final String DELETED_COLUMN = "isDeleted";

    @Autowired
    public JDBCBackgroundCategoryDAO(DBHandler handler) throws PersistenceException {
        try {
            con = handler.getConnection();
        } catch (DatabaseException e) {
            throw new PersistenceException(e);
        }
    }

    private static final String CREATE_STATEMENT =
            QueryBuilder.buildInsert(TABLE_NAME, NAME_COLUMN);

    @Override
    public Background.Category create(Background.Category backgroundCategory) throws PersistenceException {

        if (backgroundCategory==null)
            throw new IllegalArgumentException("Error! Called create method with null pointer.");


        try (PreparedStatement stmt = con.prepareStatement(CREATE_STATEMENT, Statement.RETURN_GENERATED_KEYS)){
            stmt.setString(1,backgroundCategory.getName());
            stmt.executeUpdate();
            //Get autoassigned id
            try(ResultSet rs = stmt.getGeneratedKeys()) {
                rs.next();
                int newID = rs.getInt(1);
                LOGGER.trace("Background category persisted successfully: {}", backgroundCategory);
                return new Background.Category(newID, backgroundCategory.getName(), backgroundCategory.isDeleted());
            }
        }
        catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    private static final String UPDATE_STATEMENT =
            QueryBuilder.buildUpdate(TABLE_NAME, NAME_COLUMN, ID_COLUMN);

    @Override
    public void update(Background.Category backgroundCategory) throws PersistenceException {
        if(backgroundCategory==null)
            throw new IllegalArgumentException("Error! Called update method with null pointer.");


        try (PreparedStatement stmt = con.prepareStatement(UPDATE_STATEMENT)) {
            stmt.setString(1,backgroundCategory.getName());
            stmt.setInt(2,backgroundCategory.getId());
            int returnUpdateCount = stmt.executeUpdate();

            // Check, if object has been updated and return suitable boolean value or throw Exception
            if (returnUpdateCount == 1){
                LOGGER.trace("Background category has been successfully updated to {}", backgroundCategory);
            }
            else if (returnUpdateCount == 0){
                throw new PersistenceException("Provided background category has not been updated, since it doesn't exist in the database.");
            } else {
                throw new PersistenceException("Consistency of persistence store is broken! Multiple rows have been updated.");
            }
        }
        catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    private static final String READ_ONE_STATEMENT =
            QueryBuilder.buildSelectAllColumns(TABLE_NAME, ID_COLUMN);

    @Override
    public Background.Category read(int id) throws PersistenceException {

        try (PreparedStatement stmt = this.con.prepareStatement(READ_ONE_STATEMENT)){
            stmt.setInt(1,id);
            try(ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Background.Category backgroundcategory
                            = new Background.Category(
                            rs.getInt(ID_COLUMN),
                            rs.getString(NAME_COLUMN),
                            rs.getBoolean(DELETED_COLUMN));
                    LOGGER.trace("Requested background category has been read successfully: {}", backgroundcategory);
                    return backgroundcategory;
                } else {
                    throw new PersistenceException("No background category with the given id could be found");
                }
            }
        }
        catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    private static final String READ_ALL_STATEMENT =
            QueryBuilder.buildSelectAllColumns(TABLE_NAME, DELETED_COLUMN);

    @Override
    public List<Background.Category> readAll() throws PersistenceException {
        try (PreparedStatement stmt = con.prepareStatement(READ_ALL_STATEMENT)){
            stmt.setBoolean(1, false);
            try(ResultSet rs = stmt.executeQuery()){
                List<Background.Category> returnList = new ArrayList<>();

                while (rs.next()) {
                    Background.Category backgroundcategory = new Background.Category(
                            rs.getInt(ID_COLUMN),
                            rs.getString(NAME_COLUMN),
                            rs.getBoolean(DELETED_COLUMN));
                    returnList.add(backgroundcategory);
                }
                LOGGER.trace("Background categories have been read from database: {}", returnList);
                return returnList;
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    private static final String READ_ALL_OF_PROFILE_STATEMENT =
            "SELECT b.* FROM backgroundcategories b " +
                    "left join profile_backgroundcategories p " +
            "on b.backgroundcategoryid=p.backgroundcategoryid " +
                    "where isDeleted = 'false' and p.profileid = ?;";

    @Override
    public List<Background.Category> getAllCategoriesForProfile(int profileID) throws PersistenceException {
        try (PreparedStatement stmt = con.prepareStatement(READ_ALL_OF_PROFILE_STATEMENT)){
            stmt.setInt(1,profileID);
            try(ResultSet rs = stmt.executeQuery()){
                List<Background.Category> returnList = new ArrayList<>();

                while (rs.next()) {
                    Background.Category backgroundcategory
                            = new Background.Category(
                            rs.getInt(ID_COLUMN),
                            rs.getString(NAME_COLUMN),
                            rs.getBoolean(DELETED_COLUMN));
                    returnList.add(backgroundcategory);
                }
                LOGGER.trace("Background categories for profile with id {} have been read from persistence: {}",profileID, returnList);
                return returnList;
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    private static final String DELETE_STATEMENT =
            QueryBuilder.buildUpdate(TABLE_NAME, DELETED_COLUMN, new String[]{ID_COLUMN, DELETED_COLUMN});


    @Override
    public void delete(Background.Category backgroundCategory) throws PersistenceException {
        if (backgroundCategory==null)
            throw new IllegalArgumentException("Error!:Called delete method with null pointer.");


        try (PreparedStatement stmt = con.prepareStatement(DELETE_STATEMENT)){
            stmt.setBoolean(1,true);
            stmt.setInt(2,backgroundCategory.getId());
            stmt.setBoolean(3, false);
            int returnUpdateCount  = stmt.executeUpdate();

            // Check, if object has been updated and return suitable boolean value
            if (returnUpdateCount == 1){
                LOGGER.trace("Background category {} has been successfully deleted", backgroundCategory);
            }
            else if (returnUpdateCount == 0){
                throw new PersistenceException("Provided background category has not been deleted, since it doesn't exist in the database.");
            }
            else {
                throw new PersistenceException("Consistency of persistence store is broken, multiple entries have been deleted!");
            }

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }


    private static final String CREATE_RELATION_STATEMENT =
            QueryBuilder.buildInsert("profile_backgroundcategories", new String[]{"backgroundcategoryid", "name"});

    @Override
    public void createProfileCategoryRelation(int profileID, int categoryID) throws PersistenceException {

        try (PreparedStatement stmt = con.prepareStatement(CREATE_RELATION_STATEMENT, Statement.RETURN_GENERATED_KEYS)){
            stmt.setInt(1, profileID);
            stmt.setInt(2, categoryID);
            stmt.executeUpdate();

            LOGGER.trace("Relation between profile with id {} and background category with id {} successfully persisted", profileID, categoryID);

        }catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    private static final String DELETE_RELATION_STATEMENT =
            QueryBuilder.buildDelete("profile_backgroundcategories", new String[]{"profileid","backgroundcategoryid"});

    @Override
    public void deleteProfileCategoryRelation(int profileID, int categoryID) throws PersistenceException {
        try (PreparedStatement stmt = con.prepareStatement(DELETE_RELATION_STATEMENT)){
            stmt.setInt(1,profileID);
            stmt.setInt(2,categoryID);
            stmt.execute();
            LOGGER.trace("Relation between profile with id {} and background category with id {} successfully deleted", profileID, categoryID);
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }
}
