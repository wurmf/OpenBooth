package at.ac.tuwien.sepm.ws16.qse01.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Profile entity
 */
public class Profile {
    private int id;
    private String name;
    private List<PairCameraPosition> pairCameraPositions;
    private List<PairLogoRelativeRectangle> pairLogoRelativeRectangles;
    private List<Background.Category> backgroundCategories = new ArrayList<>();
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
                   boolean isMobilEnabled,
                   String watermark
    ) {this(Integer.MIN_VALUE,
            name,
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            isPrintEnabled,
            isFilterEnabled,
            isGreenscreenEnabled,
            isMobilEnabled,
            watermark,
            false);
    }

    /**
     * Constructor with known ID
     *
     * @param id - positive profile id
     * @param name - no empty profile name
     * @param pairCameraPositions - no null, but maybe an empty list
     * @param pairLogoRelativeRectangles - no null, but maybe an empty list
     * @param watermark - String up to 250 chars, no null, but maybe an empty string
     */
    public Profile(int id,
                   String name,
                   List<PairCameraPosition> pairCameraPositions,
                   List<PairLogoRelativeRectangle> pairLogoRelativeRectangles,
                   List<Background.Category> backgroundCategories,
                   boolean isPrintEnabled,
                   boolean isFilterEnabled,
                   boolean isGreenscreenEnabled,
                   boolean isMobilEnabled,
                   String watermark,
                   boolean isDeleted
                   ) {
        this.id = id;
        this.name = name;
        if (pairCameraPositions != null)
            {this.pairCameraPositions = pairCameraPositions;}
        else
            {this.pairCameraPositions = new ArrayList<>();}
        if (pairLogoRelativeRectangles != null)
            {this.pairLogoRelativeRectangles = pairLogoRelativeRectangles;}
        else
            {this.pairLogoRelativeRectangles = new ArrayList<>();}
        if (backgroundCategories != null)
            {this.backgroundCategories = backgroundCategories;}
        else
            {this.backgroundCategories = new ArrayList<>();}
        this.isPrintEnabled = isPrintEnabled;
        this.isFilerEnabled = isFilterEnabled;
        this.isGreenscreenEnabled = isGreenscreenEnabled;
        this.isMobilEnabled = isMobilEnabled;
        if (watermark != null)
            {this.watermark = watermark;}
        else
            {this.watermark = "";}
        this.isDeleted = isDeleted;
    }

    /**
     * Constructor with unknown id will set id to Integer.MIN_VALUE and persistence layer has
     * to provide a valid id
     *
     * @param name - no empty profile name
     */
    public Profile(String name){
        this(Integer.MIN_VALUE,
                name,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                false,
                false,
                false,
                false,
                "",
                false);
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

    public List<PairCameraPosition> getPairCameraPositions() {
        return pairCameraPositions;
    }

    public void setPairCameraPositions(List<PairCameraPosition> pairCameraPositions) {
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

    public List<Background.Category> getBackgroundCategories() {
        return backgroundCategories;
    }

    public void setBackgroundCategories(List<Background.Category> backgroundCategories) {
        this.backgroundCategories = backgroundCategories;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public boolean equals(Object object){
        return object instanceof Profile
                && ((Profile) object).getId() == this.getId();
    }

    @Override
    public String toString(){return this.name;}

    /**
     * Camera-Position Pair Entity
     * Note: Contains private attribute filterName, background and shotType that are not persisted
     */
    public static class PairCameraPosition {
        private int id;
        private int profileId;
        private Camera camera;
        private Position position;
        private String filterName = "";
        private Background background;
        private boolean isGreenScreenReady;
        public static final int SHOT_TYPE_TIMED = 2;
        public static final int SHOT_TYPE_SINGLE = 0;
        public static final int SHOT_TYPE_MULTIPLE = 1;
        private int shotType;

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
        public String getCameraLable(){ return this.camera.getLable();}
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

        public String getFilterName() {
            return this.filterName;
        }

        public void setFilterName(String filterName) {
            this.filterName = filterName;
        }

        public int getShotType() {
            return shotType;
        }

        public void setShotType(int shotType) {
            this.shotType = shotType;
        }

        public Background getBackground() {
            return background;
        }

        public void setBackground(Background background) {
            this.background = background;
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
                    && ((PairCameraPosition) object).getId() == this.getId();
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
                    && ((PairLogoRelativeRectangle) object).getId() == this.getId();
        }
    }
}
