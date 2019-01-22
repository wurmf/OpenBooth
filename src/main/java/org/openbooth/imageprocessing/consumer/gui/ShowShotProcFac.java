package org.openbooth.imageprocessing.consumer.gui;

import org.openbooth.config.key.ConfigIntegerKeys;
import org.openbooth.gui.ShotFrameController;
import org.openbooth.imageprocessing.consumer.ImageConsumer;
import org.openbooth.imageprocessing.consumer.ImageConsumerFactory;
import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.storage.ConfigStore;
import org.openbooth.storage.exception.KeyValueStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ShowShotProcFac implements ImageConsumerFactory {

    private ShotFrameController shotFrameController;
    private ConfigStore configStore;

    public ShowShotProcFac(ShotFrameController shotFrameController, ConfigStore configStore){
        this.shotFrameController = shotFrameController;
        this.configStore = configStore;
    }

    @Override
    public ImageConsumer getConsumer() throws ProcessingException {
        try {
            int showShotTime = configStore.getInt(ConfigIntegerKeys.SHOW_SHOT_TIME.key);
            return new ShowShotProcessor(shotFrameController, showShotTime);
        } catch (KeyValueStoreException e) {
            throw new ProcessingException(e);
        }
    }
}
