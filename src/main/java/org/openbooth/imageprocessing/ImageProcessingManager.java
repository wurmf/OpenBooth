package org.openbooth.imageprocessing;

import org.openbooth.imageprocessing.exception.StopExecutionException;
import org.openbooth.imageprocessing.pipelines.ImageProcessorPipeline;
import org.openbooth.imageprocessing.pipelines.impl.PreviewPipeline;
import org.openbooth.imageprocessing.pipelines.impl.ShotPipeline;
import org.openbooth.storage.KeyValueStore;
import org.openbooth.storage.exception.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Component
public class ImageProcessingManager extends Thread {

    private static final Logger LOGGER  = LoggerFactory.getLogger(ImageProcessingManager.class);

    KeyValueStore keyValueStore;

    private ImageProcessorPipeline previewPipeline;
    private ImageProcessorPipeline shotPipeline;

    private ImageProcessorPipeline currentPipeline;
    private ImageProcessorPipeline nextPipeline;


    private boolean isOperating = false;
    private LocalTime timeOfLastExecution;
    private Duration durationBetweenExecutions;

    @Autowired
    public ImageProcessingManager(KeyValueStore keyValueStore, PreviewPipeline previewPipeline, ShotPipeline shotPipeline){
        this.keyValueStore = keyValueStore;
        this.previewPipeline = previewPipeline;
        this.shotPipeline = shotPipeline;

        nextPipeline = previewPipeline;

    }


    public synchronized void trigger() {
        nextPipeline = shotPipeline;
    }

    private synchronized void setPreviewPipelineAsNextPipeline(){
        if(currentPipeline == previewPipeline && nextPipeline == previewPipeline){
            Duration timeSinceLastExecution = Duration.between(timeOfLastExecution, LocalTime.now());
            if(timeSinceLastExecution.compareTo(durationBetweenExecutions) < 0){
                try {
                    Thread.sleep(durationBetweenExecutions.minus(timeSinceLastExecution).toMillis());
                } catch (InterruptedException e) {
                    LOGGER.error("Exception during waiting for next execution of pipeline",e);
                }
            }
        }

        nextPipeline = previewPipeline;
    }

    public synchronized void stopOperating(){
        isOperating = false;
    }

    @Override
    public void run() {
        try {
            int exeutionsPerSecond = keyValueStore.getInt("executions_per_second");
            timeOfLastExecution = LocalTime.now();
            durationBetweenExecutions = Duration.of(1, ChronoUnit.SECONDS).dividedBy(exeutionsPerSecond);
        } catch (PersistenceException e) {
            LOGGER.error("Error during operator initialization", e);
            return;
        }

        try {
            isOperating = true;
            while(isOperating){
                List<BufferedImage> currentImages = new ArrayList<>();
                currentPipeline = nextPipeline;
                setPreviewPipelineAsNextPipeline();
                currentPipeline.startProcessing(currentImages);
                timeOfLastExecution = LocalTime.now();
            }
            LOGGER.info("ImageProcessor execution stopped.");
        } catch (StopExecutionException e) {
            LOGGER.error("ImageProcessor execution terminated!");
        }
    }
}
