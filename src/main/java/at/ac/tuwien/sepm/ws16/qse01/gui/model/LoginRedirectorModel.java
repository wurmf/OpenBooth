package at.ac.tuwien.sepm.ws16.qse01.gui.model;

/**
 * Used by WindowManager and LoginFrameController to share which scene is to be set next.
 */
public interface LoginRedirectorModel {
    /**
     * Set the number of the next scene that shall be shown and the scene from which the login-check is called.
     * @param nextScene one of the static numbers defined in WindowManager for choosing a frame, representing the window which shall be shown next if the credentials are correct.
     * @param callingScene one of the static numbers defined in WindowManager for choosing a frame, representing the window which shall be shown next if the back-button is clicked.
     */
    void setScenes(int nextScene, int callingScene);

    /**
     * Get the number of the next window that shall be shown. If setScene() was not called previously an integer is given that is out of range for the WindowManager
     * which will subsequently forward the user to the MainScene without any notice of error. A call of getNextScene() will also reset the previously set value
     * to the default out-of-range value, which means that a set value can only be retrieved once.
     * @return the number of the next scene that shall be shown.
     */
    int getNextScene();

    /**
     * Get the number of the scene from which the Login-check was called.
     * @return the number of the calling scene.
     */
    int getCallingScene();
}
