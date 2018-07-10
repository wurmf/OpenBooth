package org.openbooth.entities;

/**
 * Shooting entity
 */
public class Shooting {
    private int id;
    private String storageDir;
    private boolean isActive;
    private int profileid;
    private String bgPictureFolder;

    public Shooting(int id,int profileid, String storageDir, String bgPictureFolder, boolean isActive) {
        this.id = id;
        this.profileid=profileid;
        this.storageDir = storageDir;
        this.isActive = isActive;
        this.bgPictureFolder = bgPictureFolder;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public String getBgPictureFolder() {
        return bgPictureFolder;
    }

    public void setBgPictureFolder(String bgPictureFolder) {
        this.bgPictureFolder = bgPictureFolder;
    }

    @Override
    public String toString() {
        return "Shooting{" +
                "id=" + id +
                ", storageDir='" + storageDir + '\'' +
                ", bgPictureFolder" + bgPictureFolder + '\'' +
                ", isActive=" + isActive +
                ", profileid=" + profileid +
                '}';
    }
}
