package org.openbooth.imageprocessing.processors.gui;

import org.openbooth.gui.ShotFrameController;
import org.openbooth.imageprocessing.processors.ImageProcessor;
import org.openbooth.imageprocessing.processors.ImageProcessorFactory;
import org.springframework.stereotype.Component;

@Component
public class ShowPreviewProcFac implements ImageProcessorFactory {

    private ShotFrameController shotFrameController;

    public ShowPreviewProcFac(ShotFrameController shotFrameController){
        this.shotFrameController = shotFrameController;
    }

    @Override
    public ImageProcessor getProcessor() {
        return new ShowPreviewProcessor(shotFrameController);
    }
}
