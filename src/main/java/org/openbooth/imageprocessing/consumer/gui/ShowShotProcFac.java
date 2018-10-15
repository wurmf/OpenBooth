package org.openbooth.imageprocessing.consumer.gui;

import org.openbooth.gui.ShotFrameController;
import org.openbooth.imageprocessing.consumer.ImageConsumer;
import org.openbooth.imageprocessing.consumer.ImageConsumerFactory;
import org.openbooth.imageprocessing.processors.ImageProcessorFactory;
import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.processors.ImageProcessor;
import org.openbooth.storage.KeyValueStore;
import org.openbooth.storage.exception.PersistenceException;
import org.springframework.stereotype.Component;

@Component
public class ShowShotProcFac implements ImageConsumerFactory {

    private ShotFrameController shotFrameController;
    private KeyValueStore keyValueStore;

    public ShowShotProcFac(ShotFrameController shotFrameController, KeyValueStore keyValueStore){
        this.shotFrameController = shotFrameController;
        this.keyValueStore = keyValueStore;
    }

    @Override
    public ImageConsumer getConsumer() throws ProcessingException {
        try {
            int showShotTime = keyValueStore.getInt("show_shot_time");
            return new ShowShotProcessor(shotFrameController, showShotTime);
        } catch (PersistenceException e) {
            throw new ProcessingException(e);
        }
    }
}
