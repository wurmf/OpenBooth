package org.openbooth.imageprocessing.processors.gui;

import org.openbooth.gui.ShotFrameController;
import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.processors.ImageProcessor;

import java.awt.image.BufferedImage;
import java.util.List;

public class ShowShotsProcessor implements ImageProcessor {

    private ShotFrameController shotFrameController;
    private int showTime;

    ShowShotsProcessor(ShotFrameController shotFrameController, int showTime){
        this.shotFrameController = shotFrameController;
        this.showTime = showTime;
    }

    @Override
    public void process(List<BufferedImage> images) throws ProcessingException {
        for(BufferedImage image : images){
            shotFrameController.refreshShot(image);
            try {
                Thread.sleep(showTime);
            } catch (InterruptedException e) {
                throw new ProcessingException(e);
            }
        }

    }
}
