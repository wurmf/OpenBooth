package org.openbooth.operating.gui;

import org.openbooth.gui.ShotFrameController;
import org.openbooth.operating.exception.OperationExecutionException;
import org.openbooth.operating.operations.Operation;

import java.awt.image.BufferedImage;
import java.util.List;

public class ShowShotsOperation implements Operation {

    private ShotFrameController shotFrameController;
    private int showTime;

    ShowShotsOperation(ShotFrameController shotFrameController, int showTime){
        this.shotFrameController = shotFrameController;
        this.showTime = showTime;
    }

    @Override
    public void execute(List<BufferedImage> images) throws OperationExecutionException{
        for(BufferedImage image : images){
            shotFrameController.refreshShot(image);
            try {
                Thread.sleep(showTime);
            } catch (InterruptedException e) {
                throw new OperationExecutionException(e);
            }
        }

    }
}
