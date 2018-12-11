package org.openbooth.imageprocessing.processors.storage;

import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.processors.ImageProcessor;
import org.openbooth.util.imageupload.LycheeUploadThread;

import java.awt.image.BufferedImage;

public class SaveAndUploadProcessor implements ImageProcessor {

    private SaveImagesProcessor saveProcessor;
    private String imagePath;
    private LycheeUploadThread lupThread;

    SaveAndUploadProcessor(SaveImagesProcessor saveProcessor, String imagePath, LycheeUploadThread lupThread){
        this.saveProcessor = saveProcessor;
        this.imagePath = imagePath;
        this.lupThread = lupThread;
    }


    @Override
    public BufferedImage process(BufferedImage image) throws ProcessingException {
        // save to local folder
        this.saveProcessor.process(image);
        // initiate upload to Lychee-server
        boolean success = lupThread.upload(imagePath);

        if(!success){
            throw new ProcessingException("Unable to upload image, too many images in the queue.");
        }

        return image;

    }
}

