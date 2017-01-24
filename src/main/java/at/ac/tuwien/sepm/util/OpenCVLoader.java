package at.ac.tuwien.sepm.util;

import at.ac.tuwien.sepm.util.exceptions.LibraryLoadingException;
import org.opencv.core.Core;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * This class provides a method for loading the opencv library
 */
@Component
public class OpenCVLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenCVLoader.class);

    private boolean isLoaded = false;

    /**
     * This method loads the OpenCVLibrary from the Ressource Lib folder
     * @throws LibraryLoadingException if the operating System is not supported
     */
    public void loadLibrary() throws LibraryLoadingException{
        if(!isLoaded) {

            try {
                System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
                isLoaded = true;
                LOGGER.info("Library {} loaded from system library path", Core.NATIVE_LIBRARY_NAME);
            } catch (UnsatisfiedLinkError e) {
                LOGGER.info("Library not found in system library path , trying to load from resources");
                String lib;

                String operatingSystem = System.getProperty("os.name").toLowerCase();

                if (operatingSystem.contains("mac")) {
                    lib = "/libopencv_java320.dylib";
                } else if (operatingSystem.contains("win")){
                    lib = "/opencv_java320.dll";
                } else if(operatingSystem.contains("linux")) {
                    lib = "/libopencv_java320.so";
                }else {
                    LOGGER.error("Operating System: {} not supported", operatingSystem);
                    throw new LibraryLoadingException("Operating System not supported");
                }

                String dest=System.getProperty("user.home")+"/.fotostudio"+lib;
                FileTransfer ft=new FileTransfer();
                try {
                    ft.transfer("/lib"+lib,dest,false);
                } catch (IOException e1) {
                    LOGGER.error("loadLibrary - ",e1);
                    throw new LibraryLoadingException(e1);
                }
                System.load(dest);

                isLoaded = true;
                LOGGER.info("OpenCV Library for {} loaded at: {}", operatingSystem,dest);
            }

        }
    }

}
