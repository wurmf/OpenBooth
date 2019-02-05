package org.openbooth.imageprocessing.consumer.gui;

import org.openbooth.application.ApplicationContextProvider;
import org.openbooth.gui.ShotFrameController;
import org.openbooth.imageprocessing.consumer.ImageConsumer;
import org.openbooth.imageprocessing.consumer.ImageConsumerFactory;



public class ShowPreviewProcFac implements ImageConsumerFactory {

    private ShotFrameController shotFrameController;

    public ShowPreviewProcFac(){
        this.shotFrameController = ApplicationContextProvider.getApplicationContext().getBean(ShotFrameController.class);
    }

    public ShowPreviewProcFac(ShotFrameController shotFrameController){
        this.shotFrameController = shotFrameController;
    }


    @Override
    public ImageConsumer getConsumer() {
        return new ShowPreviewProcessor(shotFrameController);
    }
}
