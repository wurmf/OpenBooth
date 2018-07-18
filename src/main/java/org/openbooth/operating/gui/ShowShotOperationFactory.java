package org.openbooth.operating.gui;

import org.openbooth.gui.ShotFrameController;
import org.openbooth.operating.OperationFactory;
import org.openbooth.operating.operations.Operation;
import org.springframework.stereotype.Component;

@Component
public class ShowShotOperationFactory implements OperationFactory {

    private static final int SHOW_SHOT_TIME = 3000;

    private ShotFrameController shotFrameController;

    public ShowShotOperationFactory(ShotFrameController shotFrameController){
        this.shotFrameController = shotFrameController;
    }

    @Override
    public Operation getOperation() {
        return new ShowShotsOperation(shotFrameController, SHOW_SHOT_TIME);
    }
}
