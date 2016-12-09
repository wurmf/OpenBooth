package at.ac.tuwien.sepm.ws16.qse01.entities;

/**
 * Camera-Position-Entity
 */
public class ProfileCameraPosition {
    private long profileId;
    private Camera camera;
    private Position position;
    private boolean isGreenScreenReady;

    public long getProfileId() {
        return profileId;
    }

    public void setProfileId(long profileId) {
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
}
