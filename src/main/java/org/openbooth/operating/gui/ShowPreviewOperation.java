package org.openbooth.operating.gui;

import org.openbooth.gui.ShotFrameController;
import org.openbooth.operating.operations.Operation;
import org.openbooth.operating.exception.OperationExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.image.BufferedImage;
import java.util.List;

public class ShowPreviewOperation implements Operation {

    private ShotFrameController shotFrameController;


    ShowPreviewOperation(ShotFrameController shotFrameController){
        this.shotFrameController = shotFrameController;
    }

    @Override
    public void execute(List<BufferedImage> images) {
        shotFrameController.refreshShot(images.get(0));
    }
}
