package at.ac.tuwien.sepm.ws16.qse01.entities;

import java.util.List;

/**
 * Profile entity
 */
public class Profile {
    private int id;
    private String name;
    private List<PairCameraPosition> pairCameraPositions;
    private List<PairLogoRelativeRectangle> pairLogoRelativeRectangles;
    private boolean isMobilEnabled;
    private boolean isPrintEnabled;
    private boolean isFilerEnabled;
    private boolean isGreenscreenEnabled;
    private String watermark;
    private boolean isDeleted;

    /**
     * Constructor with unknown ID and other information about profile
     *
     * @param name - no empty profile name
     */
    public Profile(String name,
                   boolean isPrintEnabled,
                   boolean isFilterEnabled,
                   boolean isGreenscreenEnabled,
                   boolean isMobilEnabled
    ) {
        this.id = Integer.MIN_VALUE;
        this.name = name;
        this.isPrintEnabled = isPrintEnabled;
        this.isFilerEnabled = isFilterEnabled;
        this.isGreenscreenEnabled = isGreenscreenEnabled;
        this.isMobilEnabled = isMobilEnabled;
    }

    /**
     * Constructor with known ID
     *
     * @param id - positive profile id
     * @param name - no empty profile name
     */
    public Profile(int id,
                   String name,
                   List<PairCameraPosition> pairCameraPositions,
                   List<PairLogoRelativeRectangle> pairLogoRelativeRectangles,
                   boolean isPrintEnabled,
                   boolean isFilterEnabled,
                   boolean isGreenscreenEnabled,
                   boolean isMobilEnabled,
                   boolean isDeleted
                   ) {
        this.id = id;
        this.name = name;
        this.pairCameraPositions = pairCameraPositions;
        this.pairLogoRelativeRectangles = pairLogoRelativeRectangles;
        this.isPrintEnabled = isPrintEnabled;
        this.isFilerEnabled = isFilterEnabled;
        this.isGreenscreenEnabled = isGreenscreenEnabled;
        this.isMobilEnabled = isMobilEnabled;
        this.isDeleted = isDeleted;
    }

    /**
     * Constructor with unknown id will set id to Integer.MIN_VALUE and persistence layer has
     * to provide a valid id
     *
     * @param name - no empty profile name
     */
    public Profile(String name) {
        this(Integer.MIN_VALUE, name,null,null,false,false,false,false,false);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getWatermark() {
        return watermark;
    }

    public void setWatermark(String watermark) {
        this.watermark = watermark;
    }

    public boolean isMobilEnabled() {
        return isMobilEnabled;
    }

    public void setMobilEnabled(boolean mobilEnabled) {
        isMobilEnabled = mobilEnabled;
    }

    public String toString(){return this.name;}

    /**
     * Camera-Position Pair Entity
     */
    public static class PairCameraPosition {
        private int id;
        private int profileId;
        private Camera camera;
        private Position position;
        private boolean isGreenScreenReady;

        public PairCameraPosition(int id,
                                  int profileId,
                                  Camera camera,
                                  Position position,
                                  boolean isGreenScreenReady){
            this.id = id;
            this.profileId = profileId;
            this.camera = camera;
            this.position = position;
            this.isGreenScreenReady = isGreenScreenReady;
        }

        public PairCameraPosition(int profileId,
                                  Camera camera,
                                  Position position,
                                  boolean isGreenScreenReady){
            this(Integer.MIN_VALUE,profileId,camera, position,isGreenScreenReady);
        }

        public PairCameraPosition(Camera camera,
                                  Position position,
                                  boolean isGreenScreenReady) {
            this(Integer.MIN_VALUE,Integer.MIN_VALUE,camera, position,isGreenScreenReady);
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getProfileId() {
            return profileId;
        }

        public void setProfileId(int profileId) {
            this.profileId = profileId;
        }

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

        @Override
        public boolean equals(Object object){
            return object instanceof PairCameraPosition
                    && ((PairCameraPosition) object).getId() == this.getId()
                    && ((PairCameraPosition) object).getProfileId() == this.getProfileId()
                    && ((PairCameraPosition) object).getCamera().equals(this.getCamera())
                    && ((PairCameraPosition) object).getPosition().equals(this.getPosition())
                    && ((PairCameraPosition) object).isGreenScreenReady() == this.isGreenScreenReady();
        }

    }

    /**
     * Logo-RelativeRectangle Pair Entity
     */
    public static class PairLogoRelativeRectangle {
        private int id;
        private int profileId;
        private Logo logo;
        private RelativeRectangle relativeRectangle;

        public PairLogoRelativeRectangle(int id,
                                         int profileId,
                                         Logo logo,
                                         RelativeRectangle relativeRectangle) {
            this.id = id;
            this.profileId = profileId;
            this.logo = logo;
            this.relativeRectangle = relativeRectangle;
        }

        public PairLogoRelativeRectangle(int profileId,
                                         Logo logo,
                                         RelativeRectangle relativeRectangle) {
            this(Integer.MIN_VALUE, profileId, logo, relativeRectangle);
        }

        public PairLogoRelativeRectangle(Logo logo, RelativeRectangle relativeRectangle) {
            this(Integer.MIN_VALUE, Integer.MIN_VALUE, logo, relativeRectangle);
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getProfileId() {
            return profileId;
        }

        public void setProfileId(int profileId) {
            this.profileId = profileId;
        }

        public Logo getLogo() {
            return logo;
        }

        public void setLogo(Logo logo) {this.logo = logo;}

        public RelativeRectangle getRelativeRectangle() {
            return relativeRectangle;
        }

        public void setRelativeRectangle(RelativeRectangle relativeRectangle) {
            this.relativeRectangle = relativeRectangle;
        }

        public String getPath(){
            return this.logo.getPath();
        }

        @Override
        public boolean equals(Object object){
            return object instanceof PairLogoRelativeRectangle
                    && ((PairLogoRelativeRectangle) object).getId() == this.getId()
                    && ((PairLogoRelativeRectangle) object).getProfileId() == this.getProfileId()
                    && ((PairLogoRelativeRectangle) object).getLogo().equals(this.getLogo())
                    && ((PairLogoRelativeRectangle) object).getRelativeRectangle().equals(this.getRelativeRectangle());
        }
    }
}
