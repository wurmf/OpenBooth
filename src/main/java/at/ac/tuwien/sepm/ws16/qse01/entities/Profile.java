package at.ac.tuwien.sepm.ws16.qse01.entities;

import at.ac.tuwien.sepm.ws16.qse01.camera.exeptions.CameraException;

import java.util.List;

/**
 * Profile entity
 */
public class Profile {
    private long id;
    private String name;
    private List<CameraPosition> cameraPositions;
    private List<LogoRposition> logoRpositions;
    private boolean isPrintEnabled;
    private boolean isFilerEnabled;
    private boolean isGreenscreenEnabled;
    private boolean isDeleted;

    /**
     * Constructor with known ID
     *
     * @param id - positive profile id
     * @param name - no empty profile name
     */
    public Profile(long id,
                   String name,
                   List<CameraPosition> cameraPositions,
                   List<LogoRposition> logoRpositions,
                   boolean isPrintEnabled,
                   boolean isFilterEnabled,
                   boolean isGreenscreenEnabled,
                   boolean isDeleted
                   ) {
        this.id = id;
        this.name = name;
        this.cameraPositions = cameraPositions;
        this.logoRpositions = logoRpositions;
        this.isPrintEnabled = isPrintEnabled;
        this.isFilerEnabled = isFilterEnabled;
        this.isGreenscreenEnabled = isGreenscreenEnabled;
        this.isDeleted = isDeleted;
    }

    /**
     * Constructor with unknown id will set id to Long.MIN_VALUE and persistence layer has
     * to provide a valid id
     *
     * @param name - no empty profile name
     */
    public Profile(String name) {
        this(Long.MIN_VALUE, name,null,null,false,false,false,false);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CameraPosition> getCameraPositions() {
        return cameraPositions;
    }

    public void setCameraPositions(List<CameraPosition> cameraPositions) {
        this.cameraPositions = cameraPositions;
    }

    public List<LogoRposition> getLogoRpositions() {
        return logoRpositions;
    }

    public void setLogoRpositions(List<LogoRposition> logoRpositions) {
        this.logoRpositions = logoRpositions;
    }

    public boolean isPrintEnabled() {
        return isPrintEnabled;
    }

    public void setPrintEnabled(boolean printEnabled) {
        isPrintEnabled = printEnabled;
    }

    public boolean isFilerEnabled() {
        return isFilerEnabled;
    }

    public void setFilerEnabled(boolean filerEnabled) {
        isFilerEnabled = filerEnabled;
    }

    public boolean isGreenscreenEnabled() {
        return isGreenscreenEnabled;
    }

    public void setGreenscreenEnabled(boolean greenscreenEnabled) {
        isGreenscreenEnabled = greenscreenEnabled;
    }

    public String toString(){return this.name;}
}
