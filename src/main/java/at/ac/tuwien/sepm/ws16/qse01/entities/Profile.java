package at.ac.tuwien.sepm.ws16.qse01.entities;

import java.util.List;

/**
 * Profile entity
 */
public class Profile {
    private long id;
    private String name;
    private List<ProfileCameraPosition> profileCameraPositions;
    private List<ProfileLogoRposition> profileLogoRpositions;
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
                   List<ProfileCameraPosition> profileCameraPositions,
                   List<ProfileLogoRposition> profileLogoRpositions,
                   boolean isPrintEnabled,
                   boolean isFilterEnabled,
                   boolean isGreenscreenEnabled,
                   boolean isDeleted
                   ) {
        this.id = id;
        this.name = name;
        this.profileCameraPositions = profileCameraPositions;
        this.profileLogoRpositions = profileLogoRpositions;
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

    public List<ProfileCameraPosition> getCameraPositions() {
        return profileCameraPositions;
    }

    public void setCameraPositions(List<ProfileCameraPosition> profileCameraPositions) {
        this.profileCameraPositions = profileCameraPositions;
    }

    public List<ProfileLogoRposition> getProfileLogoRpositions() {
        return profileLogoRpositions;
    }

    public void setProfileLogoRpositions(List<ProfileLogoRposition> profileLogoRpositions) {
        this.profileLogoRpositions = profileLogoRpositions;
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
