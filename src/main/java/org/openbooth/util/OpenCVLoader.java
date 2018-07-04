package org.openbooth.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * This class provides a method for loading the OpenCV library
 */
@Component
public class OpenCVLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenCVLoader.class);

    private boolean isLoaded = false;

    /**
     * This method loads the OpenCV library using the loader provided by OpenPNP/OpenCV
     */
    public void loadLibrary() {
        if(!isLoaded) {

            nu.pattern.OpenCV.loadShared();
            isLoaded = true;
            LOGGER.debug("OpenCV loaded by openPNP/OpenCV");

        }
    }

}
