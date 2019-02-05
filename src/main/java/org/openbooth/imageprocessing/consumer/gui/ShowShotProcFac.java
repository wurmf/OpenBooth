package org.openbooth.imageprocessing.consumer.gui;

import org.openbooth.application.ApplicationContextProvider;
import org.openbooth.config.key.ConfigIntegerKeys;
import org.openbooth.gui.ShotFrameController;
import org.openbooth.imageprocessing.consumer.ImageConsumer;
import org.openbooth.imageprocessing.consumer.ImageConsumerFactory;
import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.storage.ReadOnlyConfigStore;
import org.openbooth.storage.exception.ConfigStoreException;
import org.springframework.context.ApplicationContext;

public class ShowShotProcFac implements ImageConsumerFactory {

    private ShotFrameController shotFrameController;
    private ReadOnlyConfigStore configStore;

    public ShowShotProcFac(){
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        this.shotFrameController = applicationContext.getBean(ShotFrameController.class);
        this.configStore = applicationContext.getBean(ReadOnlyConfigStore.class);
    }

    public ShowShotProcFac(ShotFrameController shotFrameController, ReadOnlyConfigStore configStore){
        this.shotFrameController = shotFrameController;
        this.configStore = configStore;
    }

    @Override
    public ImageConsumer getConsumer() throws ProcessingException {
        try {
            int showShotTime = configStore.getInt(ConfigIntegerKeys.SHOW_SHOT_TIME.key);
            return new ShowShotProcessor(shotFrameController, showShotTime);
        } catch (ConfigStoreException e) {
            throw new ProcessingException(e);
        }
    }
}
