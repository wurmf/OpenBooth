package at.ac.tuwien.sepm.ws16.qse01.gui.model.impl;

import at.ac.tuwien.sepm.ws16.qse01.gui.model.LoginRedirectorModel;
import at.ac.tuwien.sepm.ws16.qse01.gui.model.SelectedImageShareModel;
import org.springframework.stereotype.Component;

/**
 * This class implements LoginRedirectorModel and SelectedImageShareModel, which means that an instance of this class can be used as Model satisfying both of these
 * interfaces.
 * So this class can be used for communication between WindowManager and
 */
@Component
public class UniversalModel implements LoginRedirectorModel, SelectedImageShareModel{
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
