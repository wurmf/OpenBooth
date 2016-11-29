package at.ac.tuwien.sepm.ws16.qse01.entities;


public class Camera {
    int id;
    String port;
    String model;

    public Camera(int id, String port, String model) {
        this.id = id;
        this.port = port;
        this.model = model;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
