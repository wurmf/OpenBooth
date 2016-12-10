package at.ac.tuwien.sepm.ws16.qse01.entities;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Represents a Image/Image taken by users.
 */
public class Image {
    private int imageID;
    private String imagepath;
    private int shootingid;
    private Date date;


    public Image(){
        //Empty Constructor
    }

    public Image(int imageID, String imagepath, int shootingid, Date date) {
        this.imageID = imageID;
        this.imagepath = imagepath;
        this.shootingid = shootingid;
        this.date = date;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public int getShootingid() {
        return shootingid;
    }

    public void setShootingid(int shootingid) {
        this.shootingid = shootingid;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Sets automatically timestamp for date column.
     *
     */
    public void setAutoDate(){
        this.date = Timestamp.valueOf(LocalDateTime.now());
    }

    /**
     * Image data will be returned as String
     * @return String the image data
     *
     */
    @Override
    public String toString(){
        return " id = "+this.getImageID()+" | imagepath = "+this.getImagepath()+" | shooting = "+ getShootingid()+" | date = "+getDate();
    }

}
