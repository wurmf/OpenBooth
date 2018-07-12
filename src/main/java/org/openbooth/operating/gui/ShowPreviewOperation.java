package org.openbooth.operating.gui;

import org.openbooth.gui.ShotFrameController;
import org.openbooth.operating.operations.Operation;
import org.openbooth.operating.exceptions.OperationExecutionException;

import java.awt.image.BufferedImage;

public class ShowPreviewOperation implements Operation {

    ShotFrameController shotFrameController;

    public ShowPreviewOperation(ShotFrameController shotFrameController){
        this.shotFrameController = shotFrameController;
    }

    @Override
    public BufferedImage execute(BufferedImage image) throws OperationExecutionException {
        shotFrameController.refreshShot(image);
        return image;
    }
}
