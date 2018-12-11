package org.openbooth.util.imageupload;

import at.schrer.lycheeupload.upload.LycheeUploaderHttp;
import org.apache.http.auth.AuthenticationException;
import org.openbooth.application.MainApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/**
 * This object is marked with the annotation @Lazy to only create it when it is actually referenced.
 * This will make sure that it is only then created when the upload process can be started and the albumId is available in the storage.
 */

@Component
@Scope("singleton")
@Lazy
public class LycheeUploadThread implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainApplication.class);

    private BlockingQueue<String> imageQueue;

    private LycheeUploaderHttp lup;
    private String albumId;

    public LycheeUploadThread(String albumId) throws IOException, AuthenticationException {

        // TODO: get login data from storage
        String serverAddress = "";
        String username = "";
        String password = "";

        // TODO: get albumId from storage
        this.albumId = "";

        this.lup = new LycheeUploaderHttp(serverAddress,username, password);
    }


    /* TODO: check if the thread should keep running if the queue is not empty.
    * Maybe if there are less than 2 images in the queue or ask the user if they really want to stop the upload process
    */
    @Override
    public void run() {

        int failureCounter = 0;

        // Stop if thread is inuterrupted or the upload failed 5 times in a row with an IOException

        while( !Thread.currentThread().isInterrupted() && failureCounter<5 ) {
            try{
                String imagePath = imageQueue.take();

                lup.uploadImage(albumId, imagePath);

                failureCounter = 0;

            } catch (InterruptedException e) {
                LOGGER.info("LycheeUploadThread interrupted.",e);
                Thread.currentThread().interrupt();
            } catch (IOException e) {
                LOGGER.warn("Error while uploading to Lychee", e);
                failureCounter++;
            }
        }

        if (failureCounter >= 5){
            // TODO: throw something to indicate failure
        }
    }

    /**
     * Add an image to the upload queue.
     * The return value can, in theory be false if the queue is full, but since the queue in this case does not have an actual size limit, the return should always be true.
     *
     * @param imagePath the path to the image.
     * @return true if the image could be added, false if the size restrictions do not permit adding new images.
     */
    public synchronized boolean upload(String imagePath){
        return this.imageQueue.offer(imagePath);
    }
}
