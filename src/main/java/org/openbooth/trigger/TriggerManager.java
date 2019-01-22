package org.openbooth.trigger;

import org.openbooth.context.ContextInformation;
import org.openbooth.context.ShotType;
import org.openbooth.imageprocessing.ImageProcessingManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TriggerManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(TriggerManager.class);

    private ImageProcessingManager imageProcessingManager = null;
    private ContextInformation contextInformation;

    @Autowired
    private TriggerManager(ContextInformation contextInformation){
        this.contextInformation = contextInformation;
    }

    public void setImageProcessingManager(ImageProcessingManager imageProcessingManager){
        this.imageProcessingManager = imageProcessingManager;
    }


     void triggerSingleShot(){
        contextInformation.setShotTypeTo(ShotType.SINGLE);
        triggerCurrentShotType();
    }

    void triggerTimedShot(){
        contextInformation.setShotTypeTo(ShotType.TIMED);
        triggerCurrentShotType();
    }

    void triggerCurrentShotType(){
        if(imageProcessingManager == null){
            LOGGER.debug("Trigger pressed, but image processing is not set - Ignoring trigger");
        } else {
            imageProcessingManager.trigger();
            LOGGER.debug("{} shot triggered", contextInformation.getShotType());
        }
    }
}
