package org.openbooth.operating.gui;

import org.openbooth.gui.ShotFrameController;
import org.openbooth.operating.operations.Operation;
import org.openbooth.operating.OperationFactory;
import org.springframework.stereotype.Component;

@Component
public class ShowPreviewOperationFactory implements OperationFactory {

    ShotFrameController shotFrameController;

    public ShowPreviewOperationFactory(ShotFrameController shotFrameController){
        this.shotFrameController = shotFrameController;
    }

    @Override
    public Operation getOperation() {
        return new ShowPreviewOperation(shotFrameController);
    }
}
