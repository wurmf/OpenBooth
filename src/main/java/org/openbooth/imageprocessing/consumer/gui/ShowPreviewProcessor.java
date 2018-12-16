package org.openbooth.imageprocessing.consumer.gui;

import org.openbooth.gui.ShotFrameController;
import org.openbooth.imageprocessing.consumer.ImageConsumer;

import java.awt.image.BufferedImage;

public class ShowPreviewProcessor implements ImageConsumer {

    private ShotFrameController shotFrameController;


    ShowPreviewProcessor(ShotFrameController shotFrameController){
        this.shotFrameController = shotFrameController;
    }

    @Override
    public void consume(BufferedImage image) {
        shotFrameController.refreshShot(image);
    }
}
