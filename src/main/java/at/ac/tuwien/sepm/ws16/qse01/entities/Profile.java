package at.ac.tuwien.sepm.ws16.qse01.entities;

import java.util.List;

/**
 * Profile entity
 */
public class Profile {
    private long id;
    private String name;
    private List<PairCameraPosition> pairCameraPositions;
    private List<PairLogoRelativeRectangle> pairLogoRelativeRectangles;
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
                   List<PairCameraPosition> pairCameraPositions,
                   List<PairLogoRelativeRectangle> pairLogoRelativeRectangles,
                   boolean isPrintEnabled,
                   boolean isFilterEnabled,
                   boolean isGreenscreenEnabled,
                   boolean isDeleted
                   ) {
        this.id = id;
        this.name = name;
        this.pairCameraPositions = pairCameraPositions;
        this.pairLogoRelativeRectangles = pairLogoRelativeRectangles;
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

    public List<PairCameraPosition> getCameraPositions() {
        return pairCameraPositions;
    }

    public void setCameraPositions(List<PairCameraPosition> pairCameraPositions) {
        this.pairCameraPositions = pairCameraPositions;
    }

    public List<PairLogoRelativeRectangle> getPairLogoRelativeRectangles() {
        return pairLogoRelativeRectangles;
    }

    public void setPairLogoRelativeRectangles(List<PairLogoRelativeRectangle> pairLogoRelativeRectangles) {
        this.pairLogoRelativeRectangles = pairLogoRelativeRectangles;
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
