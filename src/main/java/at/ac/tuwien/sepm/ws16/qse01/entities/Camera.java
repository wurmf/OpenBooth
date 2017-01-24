package at.ac.tuwien.sepm.ws16.qse01.entities;

/**
 * Entitiy class for camera-entries in the database.
 */
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
    public boolean equals(Object object){
        if(object instanceof Camera
                && ((Camera) object).getId()==this.getId()
                && ((Camera) object).getLable()==this.getLable()
                && ((Camera) object).getPort()==this.getPort()
                && ((Camera) object).getModel()==this.getModel()
                && ((Camera) object).getSerialnumber()==this.getSerialnumber()
                )
        {
            return true;
        }
        else {
            return false;
        }
    }
}
