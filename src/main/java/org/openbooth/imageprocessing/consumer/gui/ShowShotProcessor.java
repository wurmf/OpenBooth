package org.openbooth.imageprocessing.consumer.gui;

import org.openbooth.gui.ShotFrameController;
import org.openbooth.imageprocessing.consumer.ImageConsumer;
import org.openbooth.imageprocessing.exception.ProcessingException;

import java.awt.image.BufferedImage;

public class ShowShotProcessor implements ImageConsumer {

    private ShotFrameController shotFrameController;
    private int showTime;

    ShowShotProcessor(ShotFrameController shotFrameController, int showTime){
        this.shotFrameController = shotFrameController;
        this.showTime = showTime;
    }

    @Override
    public void consume(BufferedImage image) throws ProcessingException {

        shotFrameController.refreshShot(image);
        try {
            Thread.sleep(showTime);
        } catch (InterruptedException e) {
            throw new ProcessingException(e);
        }


    }
}
