package at.ac.tuwien.sepm.ws16.qse01.entities;

/**
 * Shooting entety
 */
public class Shooting {
    int id;
    String storageDir;
    boolean isActive;
    int profileid;

    public Shooting(int id,int profileid, String storageDir, boolean isActive) {
        this.id = id;
        this.profileid=profileid;
        this.storageDir = storageDir;
        this.isActive = isActive;
    }

    public int getProfileid() {
        return profileid;
    }

    public void setProfileid(int profileid) {
        this.profileid = profileid;
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

    @Override
    public String toString() {
        return "Shooting{" +
                "id=" + id +
                ", storageDir='" + storageDir + '\'' +
                ", isActive=" + isActive +
                ", profileid=" + profileid +
                '}';
    }
}
