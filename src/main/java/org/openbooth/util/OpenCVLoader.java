package org.openbooth.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * This class provides a method for loading the opencv library
 */
@Component
public class OpenCVLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenCVLoader.class);

    private boolean isLoaded = false;

    /**
     * This method loads the OpenCVLibrary using the loader provided by OpenPNP/opencv
     */
    public void loadLibrary() {
        if(!isLoaded) {

            nu.pattern.OpenCV.loadShared();
            isLoaded = true;
            LOGGER.debug("OpenCV loaded by openPNP/opencv");

        }
    }

}
