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

    /**
     * Camera-Position Pair Entity
     */
    public static class PairCameraPosition {
        private Camera camera;
        private Position position;
        private boolean isGreenScreenReady;

        public Camera getCamera() {
            return camera;
        }

        public void setCamera(Camera camera) {
            this.camera = camera;
        }

        public Position getPosition() {
            return position;
        }

        public void setPosition(Position position) {
            this.position = position;
        }

        public boolean isGreenScreenReady() {
            return isGreenScreenReady;
        }

        public void setGreenScreenReady(boolean greenScreenReady) {
            isGreenScreenReady = greenScreenReady;
        }
    }

    /**
     * Logo-RelativeRectangle Pair Entity
     */
    public static class PairLogoRelativeRectangle {
        private Logo logo;
        private RelativeRectangle relativeRectangle;

        public Logo getLogo() {
            return logo;
        }

        public void setLogo(Logo logo) {
            this.logo = logo;
        }

        public PairLogoRelativeRectangle(Logo logo, RelativeRectangle relativeRectangle) {
            this.logo = logo;
            this.relativeRectangle = relativeRectangle;
        }

        public RelativeRectangle getRelativeRectangle() {
            return relativeRectangle;
        }

        public void setRelativeRectangle(RelativeRectangle relativeRectangle) {
            this.relativeRectangle = relativeRectangle;
        }
    }
}
