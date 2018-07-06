package org.openbooth.dao.impl;


import org.openbooth.util.QueryBuilder;
import org.openbooth.util.dbhandler.DBHandler;
import org.openbooth.util.exceptions.DatabaseException;
import org.openbooth.dao.CameraDAO;
import org.openbooth.dao.exceptions.PersistenceException;
import org.openbooth.entities.Camera;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class JDBCCameraDAO implements CameraDAO{

    private Connection con;
    static final Logger LOGGER = LoggerFactory.getLogger(JDBCCameraDAO.class);

    private static final String TABLE_NAME = "cameras";
    private static final String ID_COLUMN = "cameraID";
    private static final String LABEL_COLUMN = "label";
    private static final String MODEL_COLUMN = "modelName";
    private static final String PORT_NUMBER_COLUMN = "portNumber";
    private static final String SERIAL_NUMBER_COLUMN = "serialNumber";
    private static final String IS_ACTIVE_COLUMN = "isActive";

    @Autowired
    public JDBCCameraDAO(DBHandler dbHandler) throws PersistenceException {
        try {
            con = dbHandler.getConnection();
        } catch (DatabaseException e) {
            throw new PersistenceException(e);
        }
    }


    private static final String CREATE_STATEMENT =
            QueryBuilder.buildInsert(TABLE_NAME, new String[]{LABEL_COLUMN,MODEL_COLUMN,PORT_NUMBER_COLUMN,SERIAL_NUMBER_COLUMN});

    @Override
    public Camera create(Camera camera) throws PersistenceException {
        if(camera == null)
            throw new IllegalArgumentException("camera is null");

        try (PreparedStatement stmt = con.prepareStatement(CREATE_STATEMENT, Statement.RETURN_GENERATED_KEYS)){


            stmt.setString(1,camera.getLable());
            stmt.setString(2,camera.getModel());

            stmt.setString (3,camera.getPort());
            stmt.setString (4,camera.getSerialnumber());

            stmt.executeUpdate();

            try(ResultSet rs = stmt.getGeneratedKeys()) {
               rs.next() ;
               Camera storedCamera = new Camera(
                       rs.getInt(1),
                       camera.getLable(),
                       camera.getPort(),
                       camera.getModel(),
                       camera.getSerialnumber()
               );
               LOGGER.trace("Camera {} successfully stored in database", storedCamera);
               return storedCamera;
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    private static final String DELETE_STATEMENT =
            QueryBuilder.buildDelete(TABLE_NAME,ID_COLUMN);

    @Override
    public void delete(int cameraID) throws PersistenceException {

        try(PreparedStatement stmt = con.prepareStatement(DELETE_STATEMENT)) {
            stmt.setInt(1,cameraID);
            stmt.execute();
            LOGGER.trace("Camera with id {} successfully deleted from database", cameraID);
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    private static final String READ_ONE_STATEMENT =
            QueryBuilder.buildSelectAllColumns(TABLE_NAME, ID_COLUMN);

    @Override
    public Camera read(int id) throws PersistenceException {

        try (PreparedStatement stmt = con.prepareStatement(READ_ONE_STATEMENT)){
            stmt.setInt(1,id);
            try (ResultSet rs = stmt.executeQuery()){
             rs.next();
             Camera camera = readCameraFromResultSet(rs);
            LOGGER.trace("Camera {} has been read successfully.", camera);
            return camera;

            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    private static final String READ_ALL_ACTIVE_STATEMENT =
            QueryBuilder.buildSelectAllColumns(TABLE_NAME, IS_ACTIVE_COLUMN);

    @Override
    public List<Camera> readActive() throws PersistenceException {

        try (PreparedStatement stmt = con.prepareStatement(READ_ALL_ACTIVE_STATEMENT)){
            stmt.setBoolean(1, true);

            try(ResultSet rs = stmt.executeQuery()) {
                List<Camera> cameraList = new ArrayList<>();
                while (rs.next()) {
                    Camera c = readCameraFromResultSet(rs);
                    cameraList.add(c);
                }
                LOGGER.trace("All active cameras read from database: {}", cameraList);
                return cameraList;
            }

        } catch (SQLException e ) {
            throw new PersistenceException(e);
        }
    }

    private static final String READ_ALL_STATEMENT =
            QueryBuilder.buildSelectAllColumns(TABLE_NAME, new String[]{});

    @Override
    public List<Camera> getAll() throws PersistenceException {

        try (PreparedStatement stmt = con.prepareStatement(READ_ALL_STATEMENT);
             ResultSet rs = stmt.executeQuery()) {

            List<Camera> cameraList = new ArrayList<>();
            while (rs.next()) {
                Camera c = readCameraFromResultSet(rs);
                cameraList.add(c);
            }
            LOGGER.trace("All cameras read successfully from database: {}", cameraList);
            return cameraList;
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }



    @Override
    public void setActive(int cameraID) throws PersistenceException {
        setActivationStatus(cameraID, true);
        LOGGER.trace("Camera with id {} successfully activated in database", cameraID);
    }

    @Override
    public void setInactive(int cameraID) throws PersistenceException {
        setActivationStatus(cameraID, false);
        LOGGER.trace("Camera with id {} successfully deactivated in database", cameraID);
    }

    private static final String SET_ACTIVATION_STATEMENT =
            QueryBuilder.buildUpdate(TABLE_NAME, IS_ACTIVE_COLUMN, ID_COLUMN);

    private void setActivationStatus(int cameraID, boolean isActivated) throws PersistenceException {
        try (PreparedStatement stmt = con.prepareStatement(SET_ACTIVATION_STATEMENT)){
            stmt.setBoolean(1, isActivated);
            stmt.setInt(2, cameraID);
            stmt.execute();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }



    private static final String SET_ALL_INACTIVE_STATEMENT =
            QueryBuilder.buildUpdate(TABLE_NAME, IS_ACTIVE_COLUMN, new String[]{});

    @Override
    public void setAllInactive() throws PersistenceException {

        try (PreparedStatement stmt = con.prepareStatement(SET_ALL_INACTIVE_STATEMENT)){
            stmt.execute();
            LOGGER.trace("All cameras successfully deactivated in database");
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    private static final String READ_CAMERA_WITH_MODEL_STATEMENT =
            QueryBuilder.buildSelectAllColumns(TABLE_NAME, MODEL_COLUMN);

    @Override
    public Camera getCameraIfExists(Camera camera) throws PersistenceException {

        try (PreparedStatement stmt = con.prepareStatement(READ_CAMERA_WITH_MODEL_STATEMENT)) {
            stmt.setString(1, camera.getModel());
            try (ResultSet rs = stmt.executeQuery()) {
                if(rs.next()){
                    Camera storedCamera = readCameraFromResultSet(rs);
                    LOGGER.trace("getCameraIfExists - Read camera {} from database", storedCamera);
                    return storedCamera;
                } else {
                    LOGGER.trace("getCameraIfExists - No camera with model {} found in idatabase", camera.getModel());
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    private static final String UPDATE_STATEMENT =
            QueryBuilder.buildUpdate(TABLE_NAME, new String[]{LABEL_COLUMN, PORT_NUMBER_COLUMN, MODEL_COLUMN, SERIAL_NUMBER_COLUMN}, ID_COLUMN);

    @Override
    public void editCamera(Camera camera) throws PersistenceException
    {
        try (PreparedStatement stmt = con.prepareStatement(UPDATE_STATEMENT)){
            stmt.setString(1,camera.getLable());
            stmt.setString(2,camera.getPort());
            stmt.setString(3,camera.getModel());
            stmt.setString(4,camera.getSerialnumber());
            stmt.setInt(5,camera.getId());
            int updateCount = stmt.executeUpdate();
            if(updateCount == 1){
                LOGGER.trace("Camera {} successfully updated in database", camera);
            }else if(updateCount == 0){
                throw new PersistenceException("No camera was updated because no camera with the given id was found in the database");
            }else{
                throw new PersistenceException("Consistency in database is violated, more than one row has been updated");
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    private Camera readCameraFromResultSet(ResultSet rs) throws SQLException{
        return new Camera(rs.getInt(ID_COLUMN),
                rs.getString(LABEL_COLUMN),
                rs.getString(PORT_NUMBER_COLUMN),
                rs.getString(MODEL_COLUMN),
                rs.getString(SERIAL_NUMBER_COLUMN));

    }
}
