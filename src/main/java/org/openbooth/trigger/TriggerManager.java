package org.openbooth.trigger;

import org.openbooth.imageprocessing.ImageProcessingManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TriggerManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(TriggerManager.class);

    private ImageProcessingManager imageProcessingManager = null;

    public void setImageProcessingManager(ImageProcessingManager imageProcessingManager){
        this.imageProcessingManager = imageProcessingManager;
    }


     void triggerShot(){
        if(imageProcessingManager == null){
            LOGGER.debug("Trigger pressed, but image processing is not set - Ignoring trigger");
        }else {
            LOGGER.debug("Shot triggered");
            imageProcessingManager.trigger();
        }
    }
}
