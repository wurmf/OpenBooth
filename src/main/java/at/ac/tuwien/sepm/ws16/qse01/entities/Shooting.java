package at.ac.tuwien.sepm.ws16.qse01.entities;

/**
 * Created by Aniela on 23.11.2016.
 */
public class Shooting {
    int id;
    String storageDir;
    boolean isActive;

    public Shooting(int id, String storageDir, boolean isActive) {
        this.id = id;
        this.storageDir = storageDir;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStorageDir() {
        return storageDir;
    }

    public void setStorageDir(String storageDir) {
        this.storageDir = storageDir;
    }

    public boolean getActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }
}
