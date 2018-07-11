package org.openbooth.entities;

import java.util.Objects;

/**
 * Entity class for camera entries in the database.
 */
public class Camera {
    private int id;
    private String lable;
    private String port;
    private String model;
    private String serialnumber;
    private boolean isActive;


    public Camera(int id, String lable, String port, String model, String serialnumber, boolean isActive) {
        this.id = id;
        this.lable = lable;
        this.port = port;
        this.model = model;
        this.serialnumber = serialnumber;
        this.isActive = isActive;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialnumber() {
        return serialnumber;
    }

    public void setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "Camera{" +
                "id=" + id +
                ", lable='" + lable + '\'' +
                ", port='" + port + '\'' +
                ", model='" + model + '\'' +
                ", serialnumber='" + serialnumber + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Camera camera = (Camera) o;
        return id == camera.id &&
                isActive == camera.isActive &&
                Objects.equals(lable, camera.lable) &&
                Objects.equals(port, camera.port) &&
                Objects.equals(model, camera.model) &&
                Objects.equals(serialnumber, camera.serialnumber);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, lable, port, model, serialnumber, isActive);
    }
}
