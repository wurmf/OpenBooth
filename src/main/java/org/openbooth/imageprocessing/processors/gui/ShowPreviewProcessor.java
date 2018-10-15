package org.openbooth.imageprocessing.processors.gui;

import org.openbooth.gui.ShotFrameController;
import org.openbooth.imageprocessing.processors.ImageProcessor;

import java.awt.image.BufferedImage;
import java.util.List;

public class ShowPreviewProcessor implements ImageProcessor {

    private ShotFrameController shotFrameController;


    ShowPreviewProcessor(ShotFrameController shotFrameController){
        this.shotFrameController = shotFrameController;
    }

    @Override
    public void process(List<BufferedImage> images) {
        shotFrameController.refreshShot(images.get(0));
    }
}
