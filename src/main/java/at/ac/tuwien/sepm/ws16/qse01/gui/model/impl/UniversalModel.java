package at.ac.tuwien.sepm.ws16.qse01.gui.model.impl;

import at.ac.tuwien.sepm.ws16.qse01.gui.model.LoginRedirectorModel;
import org.springframework.stereotype.Component;

/**
 * This class implements LoginRedirectorModel, so it can be used to communicate between the AdminLoginController and the WindowManager.
 */
@Component
public class UniversalModel implements LoginRedirectorModel{
    private int nextScene=-1;
    private int callingScene=-1;

    @Override
    public void setScenes(int nextScene, int callingScene) {
        this.nextScene=nextScene;
        this.callingScene=callingScene;
    }

    @Override
    public int getNextScene() {
        int out=nextScene;
        nextScene=-1;
        return out;
    }

    @Override
    public int getCallingScene() {
        int out=callingScene;
        callingScene=-1;
        return out;
    }
}
