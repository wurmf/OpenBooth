package at.ac.tuwien.sepm.ws16.qse01.dao.impl;


import at.ac.tuwien.sepm.util.dbhandler.DBHandler;
import at.ac.tuwien.sepm.util.exceptions.DatabaseException;
import at.ac.tuwien.sepm.ws16.qse01.dao.ShootingDAO;
import at.ac.tuwien.sepm.ws16.qse01.dao.exceptions.PersistenceException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Background;
import at.ac.tuwien.sepm.ws16.qse01.entities.Shooting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * ShootingDaoImpl
 */
@Repository
public class JDBCShootingDAO implements ShootingDAO {

    private Connection con;
    private String currentBgPath;
    private List<Background> currentBackgrounds;
    private static final Logger LOGGER = LoggerFactory.getLogger(ShootingDAO.class);


    @Autowired
    public JDBCShootingDAO(DBHandler dbHandler) throws PersistenceException {
        try {
            con = dbHandler.getConnection();
        } catch (DatabaseException e) {
            LOGGER.error("Constructor - ",e);
            throw new PersistenceException(e);
        }
        currentBgPath=null;
        currentBackgrounds=null;
    }

    @Override
    public Shooting create(Shooting shooting) throws PersistenceException {
        PreparedStatement stmt = null;
        if(shooting==null) {
            LOGGER.debug("caught nullpointer Shooting");
            throw new IllegalArgumentException("Error: Called create method with null pointer.");
        }


        try {
            String sql="INSERT INTO Shootings(profileId, folderpath, bgpicturefolder, isactive) VALUES (?,?,?,?)";

            stmt = this.con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1,shooting.getProfileid());
            stmt.setString(2,shooting.getStorageDir());
            stmt.setString(3,shooting.getBgPictureFolder());
            stmt.setBoolean(4,shooting.getActive());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()){
                shooting.setId(rs.getInt(1));
            }

        } catch (SQLException|IllegalArgumentException e) {
            LOGGER.info("create - ",e);
            throw new PersistenceException(e);
        } finally{
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOGGER.error("Select",e);
                }
            }
        }
        return shooting;
    }

    @Override
    public Shooting searchIsActive() throws PersistenceException {

        PreparedStatement stmt =null;
        Shooting shooting = new Shooting(0,0,"","",false);
        try {
            stmt = con.prepareStatement("SELECT * FROM Shootings WHERE isactive = true");
            ResultSet rst = stmt.executeQuery();
            if(rst.next()){
                shooting = new Shooting(rst.getInt("SHOOTINGID"), rst.getInt("PROFILEID"),rst.getString("FOLDERPATH"), rst.getString("BGPICTUREFOLDER"), rst.getBoolean("ISACTIVE"));
            }
        } catch (SQLException e) {
            LOGGER.info("searchIsActive - ",e);
            throw new PersistenceException(e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                   LOGGER.error("searchIsAcitve - ",e);
                }
            }
        }
        return shooting;
    }

    @Override
    public void endShooting() throws PersistenceException {
        Statement stmt=null;
        try {
            String query="UPDATE Shootings SET isactive=false WHERE isactive= true";
            stmt = con.createStatement();
            stmt.execute(query);

        } catch (SQLException e) {
            LOGGER.info("endShooting - ", e);
            throw new PersistenceException(e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOGGER.error("endShooting - ",e);
                }
            }
        }
    }

    @Override
    public void update(Shooting shooting) throws PersistenceException {
        PreparedStatement stmt=null;
        try {
            if(shooting.getBgPictureFolder()!=null && !shooting.getBgPictureFolder().isEmpty()){
                stmt = con.prepareStatement("UPDATE Shootings SET PROFILEID=?, bgpicturefolder=? WHERE SHOOTINGID= ?");
                stmt.setInt(1,shooting.getProfileid());
                stmt.setString(2, shooting.getBgPictureFolder());
                stmt.setInt(3,shooting.getId());
            } else{
                stmt = con.prepareStatement("UPDATE Shootings SET PROFILEID=? WHERE SHOOTINGID= ?");
                stmt.setInt(1,shooting.getProfileid());
                stmt.setInt(2,shooting.getId());
            }

            stmt.execute();

        } catch (SQLException e) {
            LOGGER.info("updateProfile - ", e);
            throw new PersistenceException(e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOGGER.error("updateProfile - ",e);
                }
            }
        }
    }

    @Override
    public void getUserBackgrounds(List<Background> bgList) throws PersistenceException {
        Shooting activeShooting=this.searchIsActive();
        if(activeShooting==null || activeShooting.getBgPictureFolder()==null || activeShooting.getBgPictureFolder().isEmpty()){
            return;
        }
        FileFilter filter= c-> c.getName().endsWith(".jpg")||c.getName().endsWith(".png");
        File bgFolder= new File(activeShooting.getBgPictureFolder());
        File[] pictureFiles= bgFolder.listFiles(filter);
        if(pictureFiles==null||pictureFiles.length==0)
            return;
        List<File> filesList=Arrays.asList(pictureFiles);
        Background.Category category= new Background.Category("Userdefined Backgrounds");
        for(int i=0; i<filesList.size();i++){
            try{
                Background bg=new Background("UserDef",filesList.get(i).getCanonicalPath(),category);
                bgList.add(bg);
            } catch(IOException e){
                LOGGER.error("getUserBackgrounds - ",e);
                throw new PersistenceException("Unable to get canonical path from file",e);
            }
        }
    }

}
