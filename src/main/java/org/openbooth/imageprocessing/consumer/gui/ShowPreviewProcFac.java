package org.openbooth.imageprocessing.consumer.gui;

import org.openbooth.gui.ShotFrameController;
import org.openbooth.imageprocessing.consumer.ImageConsumer;
import org.openbooth.imageprocessing.consumer.ImageConsumerFactory;
import org.openbooth.imageprocessing.processors.ImageProcessor;
import org.openbooth.imageprocessing.processors.ImageProcessorFactory;
import org.springframework.stereotype.Component;

@Component
public class ShowPreviewProcFac implements ImageConsumerFactory {

    private ShotFrameController shotFrameController;

    public ShowPreviewProcFac(ShotFrameController shotFrameController){
        this.shotFrameController = shotFrameController;
    }

    @Override
    public ImageConsumer getConsumer() {
        return new ShowPreviewProcessor(shotFrameController);
    }
}
