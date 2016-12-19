package at.ac.tuwien.sepm.ws16.qse01.entities;


public class Camera {
    int id;
    String lable;
    String port;
    String model;
    String serialnumber;

    public Camera(int id, String lable, String port, String model, String serialnumber) {
        this.id = id;
        this.lable = lable;
        this.port = port;
        this.model = model;
        this.serialnumber = serialnumber;
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
}
