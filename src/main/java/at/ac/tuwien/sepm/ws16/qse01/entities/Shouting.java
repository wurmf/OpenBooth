package at.ac.tuwien.sepm.ws16.qse01.entities;

/**
 * Created by Aniela on 23.11.2016.
 */
public class Shouting {
    int propertyId;
    String storageFile;
    boolean isactiv;

    public Shouting(int propertyId, String storageFile, boolean isactiv) {
        this.propertyId = propertyId;
        this.storageFile = storageFile;
        this.isactiv = isactiv;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    public String getStorageFile() {
        return storageFile;
    }

    public void setStorageFile(String storageFile) {
        this.storageFile = storageFile;
    }

    public boolean getIsactiv() {
        return isactiv;
    }

    public void setIsactiv(boolean isactiv) {
        this.isactiv = isactiv;
    }
}
