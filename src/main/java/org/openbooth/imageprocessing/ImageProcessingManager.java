package org.openbooth.imageprocessing;

import org.openbooth.imageprocessing.exception.StopExecutionException;
import org.openbooth.imageprocessing.pipelines.impl.PreviewPipeline;
import org.openbooth.imageprocessing.pipelines.impl.ShotPipeline;
import org.openbooth.storage.KeyValueStore;
import org.openbooth.storage.exception.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Component
public class ImageProcessingManager extends Thread {

    private static final Logger LOGGER  = LoggerFactory.getLogger(ImageProcessingManager.class);

    private KeyValueStore keyValueStore;

    private PreviewPipeline previewPipeline;
    private ShotPipeline shotPipeline;



    private boolean isOperating = false;
    private LocalTime timeOfLastExecution;
    private Duration durationBetweenExecutions;

    private boolean triggered = false;

    @Autowired
    public ImageProcessingManager(KeyValueStore keyValueStore, PreviewPipeline previewPipeline, ShotPipeline shotPipeline){
        this.keyValueStore = keyValueStore;
        this.previewPipeline = previewPipeline;
        this.shotPipeline = shotPipeline;

    }


    public synchronized void trigger() {
        triggered = true;
    }

    private void executePreviewPipeline() throws StopExecutionException{

        Duration timeSinceLastExecution = Duration.between(timeOfLastExecution, LocalTime.now());
        if(timeSinceLastExecution.compareTo(durationBetweenExecutions) < 0){
            try {
                Thread.sleep(durationBetweenExecutions.minus(timeSinceLastExecution).toMillis());
            } catch (InterruptedException e) {
                LOGGER.error("Exception during waiting for next execution of pipeline",e);
            }
        }

        previewPipeline.execute();
        timeOfLastExecution = LocalTime.now();
    }

    public synchronized void stopOperating(){
        isOperating = false;
    }

    @Override
    public void run() {
        try {
            int executionsPerSecond = keyValueStore.getInt("executions_per_second");
            timeOfLastExecution = LocalTime.now();
            durationBetweenExecutions = Duration.of(1, ChronoUnit.SECONDS).dividedBy(executionsPerSecond);
        } catch (PersistenceException e) {
            LOGGER.error("Error during operator initialization", e);
            return;
        }

        try {
            isOperating = true;
            while(isOperating){
                if(triggered) {
                    shotPipeline.execute();
                    triggered = false;
                } else {
                    executePreviewPipeline();
                }

            }
            LOGGER.info("Image processing stopped.");
        } catch (StopExecutionException e) {
            LOGGER.error("Image processing terminated!");
        }
    }
}
