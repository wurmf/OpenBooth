package org.openbooth.util;

import org.openbooth.TestSetUpException;
import org.openbooth.dao.impl.*;
import org.openbooth.entities.*;
import org.openbooth.util.dbhandler.DBHandler;
import org.openbooth.util.exceptions.DatabaseException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Scope("prototype")
public class TestDataProvider {

    private List<AdminUser> adminUsers;
    private List<Background> backgrounds;
    private List<Background.Category> backgroundCategories;
    private List<Camera> cameras;
    private List<Position> positions;
    private List<Profile.PairCameraPosition> pairCameraPositions;
    private List<Logo> logos;
    private List<Profile.PairLogoRelativeRectangle> pairLogoRelativeRectangles;
    private List<Profile> profiles;
    private List<Shooting> shootings;
    private List<Image> images;

    public TestDataProvider(DBHandler dbHandler) throws TestSetUpException{
        generateData();
        try {
            insertData(dbHandler.getConnection());
        } catch (DatabaseException e) {
            throw new TestSetUpException(e);
        }
    }

    private void generateData() throws TestSetUpException{
        generateAdminUsersAndPasswords();
        generateCameras();
        generatePositions();
        generatePairCameraPositions();
        generateLogos();
        generatePairLogoRelativeRectangles();
        generateBackgroundCategories();
        generateBackgrounds();
        generateProfiles();
        generateShootings();
        generateImages();
    }

    private void insertData(Connection connection) throws TestSetUpException {
        //This order must be kept to ensure referential integrity
        try {
            insertAdminUsersAndPasswords(connection);
            insertCameras(connection);
            insertPositions(connection);
            insertLogos(connection);
            insertBackgroundCategories(connection);
            insertBackgrounds(connection);
            insertProfiles(connection);
            insertProfileBackgroundCategoryRelations(connection);
            insertPairCameraPositions(connection);
            insertPairLogoRelativeRectangles(connection);
            insertShootings(connection);
            insertImages(connection);
            connection.commit();
        } catch (SQLException e) {
            throw new TestSetUpException("Test data could not be inserted into database", e);
        }
    }


    private void generateAdminUsersAndPasswords() throws TestSetUpException{
        adminUsers = new ArrayList<>();
        try {
            String password = "martin";
            MessageDigest md=MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes("UTF-8"));
            byte[] passwordBytes = md.digest();
            adminUsers.add(new AdminUser("martin", passwordBytes));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new TestSetUpException("test admin user could not be inserted", e);
        }
    }

    private void insertAdminUsersAndPasswords(Connection connection) throws SQLException{
        String insertStatement =
                QueryBuilder.buildInsert(JDBCAdminUserDAO.TABLE_NAME,
                        new String[]{
                                JDBCAdminUserDAO.NAME_COLUMN,
                                JDBCAdminUserDAO.PW_HASH_COLUMN
                        });
        try(PreparedStatement stmt = connection.prepareStatement(insertStatement)){
            for(AdminUser adminUser : adminUsers){
                stmt.setString(1, adminUser.getAdminName());
                stmt.setBytes(2, adminUser.getPassword());
                stmt.execute();
            }
        }
    }


    private void generateCameras(){
        cameras = new ArrayList<>();
        cameras.add(new Camera(1,"TestCamera1", "TestPort1","TestModel1", "TestSerialnumber1"));
        cameras.add(new Camera(2,"TestCamera2", "TestPort2","TestModel2", "TestSerialnumber2"));
        cameras.add(new Camera(3,"TestCamera3", "TestPort3","TestModel3", "TestSerialnumber3"));
        cameras.add(new Camera(4,"TestCamera4", "TestPort4","TestModel4", "TestSerialnumber4"));
    }

    private void insertCameras(Connection conn) throws SQLException{
        String insertStatement =
                QueryBuilder.buildInsert(JDBCCameraDAO.TABLE_NAME,
                        new String[]{
                                JDBCCameraDAO.LABEL_COLUMN,
                                JDBCCameraDAO.PORT_NUMBER_COLUMN,
                                JDBCCameraDAO.MODEL_COLUMN,
                                JDBCCameraDAO.SERIAL_NUMBER_COLUMN});

        try(PreparedStatement stmt = conn.prepareStatement(insertStatement)){
            for(Camera camera : cameras){
                stmt.setString(1, camera.getLable());
                stmt.setString(2, camera.getPort());
                stmt.setString(3, camera.getModel());
                stmt.setString(4, camera.getSerialnumber());
                stmt.execute();
            }
        }
    }



    private void generatePositions(){
        positions = new ArrayList<>();
        positions.add(new Position(1,"TestPosition1", "/no/valid/path", false));
        positions.add(new Position(1,"TestPosition2", "/no/valid/path", false));

    }

    private void insertPositions(Connection connection) throws SQLException{
        String insertStatement =
                QueryBuilder.buildInsert(JDBCPositionDAO.TABLE_NAME,
                        new String[]{
                                JDBCPositionDAO.NAME_COLUMN,
                                JDBCPositionDAO.IMAGE_PATH_COLUMN,
                                JDBCPositionDAO.IS_DELETED_COLUMN});

        try(PreparedStatement stmt = connection.prepareStatement(insertStatement)){
            for(Position position : positions){
                stmt.setString(1, position.getName());
                stmt.setString(2, position.getButtonImagePath());
                stmt.setBoolean(3, position.isDeleted());
                stmt.execute();
            }
        }
    }



    private void generatePairCameraPositions(){
        pairCameraPositions = new ArrayList<>();
        pairCameraPositions.add(new Profile.PairCameraPosition(1, 1, cameras.get(0), positions.get(0), false));
        pairCameraPositions.add(new Profile.PairCameraPosition(2, 1, cameras.get(1), positions.get(1), false));
        pairCameraPositions.add(new Profile.PairCameraPosition(3, 2, cameras.get(1), positions.get(1), false));
        pairCameraPositions.add(new Profile.PairCameraPosition(4, 2, cameras.get(0), positions.get(1), false));
        pairCameraPositions.add(new Profile.PairCameraPosition(5, 3, cameras.get(2), positions.get(0), false));
        pairCameraPositions.add(new Profile.PairCameraPosition(6, 3, cameras.get(3), positions.get(1), false));
    }

    private void insertPairCameraPositions(Connection connection) throws SQLException{
        String insertStatement =
                QueryBuilder.buildInsert(JDBCPairCameraPositionDAO.TABLE_NAME,
                        new String[]{
                                JDBCPairCameraPositionDAO.PROFILE_ID_COLUMN,
                                JDBCPairCameraPositionDAO.CAMERA_ID_COLUMN,
                                JDBCPairCameraPositionDAO.POSITION_ID_COLUMN,
                                JDBCPairCameraPositionDAO.GREEN_SCREEN_READY_COLUMN
                        });
        try(PreparedStatement stmt = connection.prepareStatement(insertStatement)){
            for(Profile.PairCameraPosition pairCameraPosition : pairCameraPositions){
                stmt.setInt(1, pairCameraPosition.getProfileId());
                stmt.setInt(2, pairCameraPosition.getCamera().getId());
                stmt.setInt(3, pairCameraPosition.getPosition().getId());
                stmt.setBoolean(4, pairCameraPosition.isGreenScreenReady());
                stmt.execute();
            }
        }
    }



    private void generateLogos(){
        logos = new ArrayList<>();
        logos.add(new Logo(1, "TestLogo1", "/no/valid/path", false));
        logos.add(new Logo(2, "TestLogo2", "/no/valid/path", false));
    }

    private void insertLogos(Connection connection) throws SQLException{
        String insertStatement =
                QueryBuilder.buildInsert(JDBCLogoDAO.TABLE_NAME,
                        new String[]{
                                JDBCLogoDAO.LABEL_COLUMN,
                                JDBCLogoDAO.PATH_COLUMN,
                                JDBCLogoDAO.IS_DELETED_COLUMN
                        });
        try(PreparedStatement stmt = connection.prepareStatement(insertStatement)){
            for(Logo logo : logos){
                stmt.setString(1, logo.getLabel());
                stmt.setString(2, logo.getPath());
                stmt.setBoolean(3, logo.isDeleted());
                stmt.execute();
            }
        }
    }



    private void generatePairLogoRelativeRectangles(){
        pairLogoRelativeRectangles = new ArrayList<>();
        pairLogoRelativeRectangles.add(new Profile.PairLogoRelativeRectangle(1, 1, logos.get(0), new RelativeRectangle(85,95,80,90)));
        pairLogoRelativeRectangles.add(new Profile.PairLogoRelativeRectangle(2, 1, logos.get(1), new RelativeRectangle(5,10,15,20)));
        pairLogoRelativeRectangles.add(new Profile.PairLogoRelativeRectangle(3, 2, logos.get(1), new RelativeRectangle(75,95,80,90)));
        pairLogoRelativeRectangles.add(new Profile.PairLogoRelativeRectangle(4, 2, logos.get(1), new RelativeRectangle(15,20,25,30)));
        pairLogoRelativeRectangles.add(new Profile.PairLogoRelativeRectangle(5, 3, logos.get(0), new RelativeRectangle(30,30,25,30)));
    }

    private void insertPairLogoRelativeRectangles(Connection connection) throws SQLException{
        String insertStatement =
                QueryBuilder.buildInsert(JDBCPairLogoRelativeRectangleDAO.TABLE_NAME,
                        new String[]{
                                JDBCPairLogoRelativeRectangleDAO.PROFILE_ID_COLUMN,
                                JDBCPairLogoRelativeRectangleDAO.LOGO_ID_COLUMN,
                                JDBCPairLogoRelativeRectangleDAO.X_COLUMN,
                                JDBCPairLogoRelativeRectangleDAO.Y_COLUMN,
                                JDBCPairLogoRelativeRectangleDAO.WIDTH_COLUMN,
                                JDBCPairLogoRelativeRectangleDAO.HEIGHT_COLUMN
                        });
        try(PreparedStatement stmt = connection.prepareStatement(insertStatement)){
            for(Profile.PairLogoRelativeRectangle pairLogoRelativeRectangle : pairLogoRelativeRectangles){
                stmt.setInt(1, pairLogoRelativeRectangle.getProfileId());
                stmt.setInt(2, pairLogoRelativeRectangle.getLogo().getId());
                stmt.setDouble(3, pairLogoRelativeRectangle.getRelativeRectangle().getX());
                stmt.setDouble(4, pairLogoRelativeRectangle.getRelativeRectangle().getY());
                stmt.setDouble(5, pairLogoRelativeRectangle.getRelativeRectangle().getWidth());
                stmt.setDouble(6, pairLogoRelativeRectangle.getRelativeRectangle().getHeight());
                stmt.execute();
            }
        }
    }



    private void generateBackgroundCategories(){
        backgroundCategories = new ArrayList<>();
        backgroundCategories.add(new Background.Category(1,"Ostern",false));
        backgroundCategories.add(new Background.Category(2, "Weihnachten", false));
        backgroundCategories.add(new Background.Category(3, "Geburtstage", false));
        backgroundCategories.add(new Background.Category(4, "Hochzeiten", false));
    }

    private void insertBackgroundCategories(Connection connection) throws SQLException{
        String insertStatement =
                QueryBuilder.buildInsert(JDBCBackgroundCategoryDAO.TABLE_NAME,
                        new String[]{
                                JDBCBackgroundCategoryDAO.NAME_COLUMN,
                                JDBCBackgroundCategoryDAO.DELETED_COLUMN
                        });
        try(PreparedStatement stmt = connection.prepareStatement(insertStatement)){
            for(Background.Category category : backgroundCategories){
                stmt.setString(1, category.getName());
                stmt.setBoolean(2, category.isDeleted());
                stmt.execute();
            }
        }
    }



    private void generateBackgrounds(){
        backgrounds = new ArrayList<>();
        backgrounds.add(new Background(1, "TestBackground1", "/no/valid/path", backgroundCategories.get(0), false));
        backgrounds.add(new Background(2, "TestBackground2", "/no/valid/path", backgroundCategories.get(0), false));
        backgrounds.add(new Background(3, "TestBackground3", "/no/valid/path", backgroundCategories.get(1), false));
    }

    private void insertBackgrounds(Connection connection) throws SQLException{
        String insertStatement =
                QueryBuilder.buildInsert(JDBCBackgroundDAO.TABLE_NAME,
                        new String[]{
                                JDBCBackgroundDAO.NAME_COLUMN,
                                JDBCBackgroundDAO.PATH_COLUMN,
                                JDBCBackgroundDAO.CATEGORY_COLUMN,
                                JDBCBackgroundDAO.DELETED_COLUMN
                        });
        try(PreparedStatement stmt = connection.prepareStatement(insertStatement)){
            for(Background background : backgrounds){
                stmt.setString(1, background.getName());
                stmt.setString(2, background.getPath());
                stmt.setInt(3, background.getCategory().getId());
                stmt.setBoolean(4, background.isDeleted());
                stmt.execute();
            }
        }
    }


    private void generateProfiles(){
        profiles = new ArrayList<>();
        profiles.add(new Profile(1,"TestProfile1",
                pairCameraPositions.subList(0,2),
                pairLogoRelativeRectangles.subList(0,2),
                backgroundCategories.subList(0,2),
                false, false, true, false,
                "no/valid/path",false));

        profiles.add(new Profile(2,"TestProfile2",
                pairCameraPositions.subList(2,4),
                pairLogoRelativeRectangles.subList(2,4),
                backgroundCategories.subList(3,4),
                false, false, true, false,
                "no/valid/path",false));

        profiles.add(new Profile(3,"TestProfile3",
                pairCameraPositions.subList(4,6),
                pairLogoRelativeRectangles.subList(4,5),
                new ArrayList<>(),
                false, true, false, false,
                "no/valid/path",false));
    }

    private void insertProfiles(Connection connection) throws SQLException {
        String insertStatement =
                QueryBuilder.buildInsert(JDBCProfileDAO.TABLE_NAME,
                        new String[]{
                                JDBCProfileDAO.NAME_COLUMN,
                                JDBCProfileDAO.PRINT_ENABLED_COLUMN,
                                JDBCProfileDAO.FILTER_ENABLED_COLUMN,
                                JDBCProfileDAO.GREEN_SCREEN_ENABLED_COLUMN,
                                JDBCProfileDAO.IS_MOBIL_COLUMN,
                                JDBCProfileDAO.WATERMARK_COLUMN,
                                JDBCProfileDAO.IS_DELETED_COLUMN
                        });
        try(PreparedStatement stmt = connection.prepareStatement(insertStatement)){
            for(Profile profile : profiles){
                stmt.setString(1, profile.getName());
                stmt.setBoolean(2, profile.isPrintEnabled());
                stmt.setBoolean(3, profile.isFilerEnabled());
                stmt.setBoolean(4, profile.isGreenscreenEnabled());
                stmt.setBoolean(5, profile.isMobilEnabled());
                stmt.setString(6, profile.getWatermark());
                stmt.setBoolean(7, profile.isDeleted());
                stmt.execute();
            }
        }
    }

    private void insertProfileBackgroundCategoryRelations(Connection connection) throws SQLException{
        String insertStatement =
                QueryBuilder.buildInsert(JDBCProfileDAO.PROFILE_CATEGORY_RELATION_TABLE_NAME,
                        new String[]{
                                JDBCProfileDAO.PROFILE_ID_COLUMN,
                                JDBCProfileDAO.CATEGORY_ID_COLUMN
                        });
        try(PreparedStatement stmt = connection.prepareStatement(insertStatement)){
            for(Profile profile : profiles){
                stmt.setInt(1, profile.getId());
                for(Background.Category category : profile.getBackgroundCategories()){
                    stmt.setInt(2, category.getId());
                    stmt.execute();
                }
            }
        }
    }



    private void generateShootings(){
        shootings = new ArrayList<>();
        shootings.add(new Shooting(1, 1, "no/valid/path", "no/valid/path", false));
        shootings.add(new Shooting(2, 2, "no/valid/path", "no/valid/path", false));
        shootings.add(new Shooting(3, 3, "no/valid/path", "no/valid/path", true));
    }

    private void insertShootings(Connection connection) throws SQLException{
        String insertStatement =
                QueryBuilder.buildInsert(JDBCShootingDAO.TABLE_NAME,
                        new String[]{
                                JDBCShootingDAO.PROFILE_ID_COLUMN,
                                JDBCShootingDAO.FOLDER_PATH_COLUMN,
                                JDBCShootingDAO.BACKGROUND_FOLDER_PATH_COLUMN,
                                JDBCShootingDAO.IS_ACTIVE_COLUMN
                        });
        try(PreparedStatement stmt = connection.prepareStatement(insertStatement)){
            for(Shooting shooting : shootings){
                stmt.setInt(1, shooting.getProfileid());
                stmt.setString(2, shooting.getStorageDir());
                stmt.setString(3, shooting.getBgPictureFolder());
                stmt.setBoolean(4, shooting.isActive());
                stmt.execute();
            }
        }
    }



    private void generateImages(){
        images = new ArrayList<>();
        images.add(new Image(1,"/no/valid/path", shootings.get(0).getId(), new Date()));
        images.add(new Image(2,"/no/valid/path", shootings.get(1).getId(), new Date()));
        images.add(new Image(3,"/no/valid/path", shootings.get(2).getId(), new Date()));
        images.add(new Image(4,"/no/valid/path", shootings.get(2).getId(), new Date()));
        images.add(new Image(5,"/no/valid/path", shootings.get(2).getId(), new Date()));
    }

    private void insertImages(Connection connection) throws SQLException{
        String insertStatement =
                QueryBuilder.buildInsert(JDBCImageDAO.TABLE_NAME,
                        new String[]{
                                JDBCImageDAO.SHOOTING_ID_COLUMN,
                                JDBCImageDAO.IMAGE_PATH_COLUMN,
                                JDBCImageDAO.TIME_COLUMN
                        });
        try(PreparedStatement stmt = connection.prepareStatement(insertStatement)){
            for(Image image : images){
                stmt.setInt(1, image.getShootingid());
                stmt.setString(2, image.getImagepath());
                stmt.setTimestamp(3, new Timestamp(image.getDate().getTime()));
                stmt.execute();
            }
        }
    }





    public List<AdminUser> getStoredAdminUsers(){return adminUsers;}
    public List<Background> getStoredBackgrounds(){
        return backgrounds;
    }
    public List<Background.Category> getStoredBackgroundCategories(){return backgroundCategories;}
    public List<Camera> getStoredCameras(){return cameras;}
    public List<Position> getStoredPostitions(){return  positions;}
    public List<Profile.PairCameraPosition> getStoredPairCameraPositions(){return pairCameraPositions;}
    public List<Logo> getStoredLogos(){return logos;}
    public List<Profile.PairLogoRelativeRectangle> getStoredPairLogoRelativeRectangles(){return pairLogoRelativeRectangles;}
    public List<Profile> getStoredProfiles(){return profiles;}
    public List<Shooting> getStoredShootings(){return shootings;}
    public List<Image> getStoredImages(){return images;}

    public Background getNewBackground(){
        return new Background(-1, "NewTestBackground", "no/valid/path", backgroundCategories.get(0), false);
    }

    public Background.Category getNewCategory(){
        return new Background.Category(-1, "NewTestCategory", false);
    }

}
