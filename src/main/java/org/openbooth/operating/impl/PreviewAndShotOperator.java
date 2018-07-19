package org.openbooth.operating.impl;

import org.openbooth.operating.Operator;
import org.openbooth.operating.exception.StopExecutionException;
import org.openbooth.operating.pipelines.OperationPipeline;
import org.openbooth.operating.pipelines.impl.PreviewOperationPipeline;
import org.openbooth.operating.pipelines.impl.ShotOperationPipeline;
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
public class PreviewAndShotOperator implements Operator {

    private static final Logger LOGGER  = LoggerFactory.getLogger(PreviewAndShotOperator.class);

    private OperationPipeline previewOperationPipeline;
    private OperationPipeline shotOperationsPipeline;

    private OperationPipeline currentPipeline;
    private OperationPipeline nextPipeline;


    private boolean isOperating = false;
    private LocalTime timeOfLastExecution;
    private Duration durationBetweenExecutions;

    @Autowired
    public PreviewAndShotOperator(PreviewOperationPipeline previewOperationPipeline, ShotOperationPipeline shotOperationPipeline){
        this.previewOperationPipeline = previewOperationPipeline;
        this.shotOperationsPipeline = shotOperationPipeline;

        nextPipeline = previewOperationPipeline;
        timeOfLastExecution = LocalTime.now();
        durationBetweenExecutions = Duration.of(1, ChronoUnit.SECONDS).dividedBy(EXECUTIONS_PER_SECOND);
    }


    @Override
    public synchronized void trigger() {
        nextPipeline = shotOperationsPipeline;
    }

    private synchronized void setPreviewPipelineAsNextPipeline(){
        if(currentPipeline == previewOperationPipeline && nextPipeline == previewOperationPipeline){
            Duration timeSinceLastExecution = Duration.between(timeOfLastExecution, LocalTime.now());
            if(timeSinceLastExecution.compareTo(durationBetweenExecutions) < 0){
                try {
                    Thread.sleep(durationBetweenExecutions.minus(timeSinceLastExecution).toMillis());
                } catch (InterruptedException e) {
                    LOGGER.error("Exception during waiting for next execution of pipeline",e);
                }
            }
        }

        nextPipeline = previewOperationPipeline;
    }

    @Override
    public synchronized void stopOperating(){
        isOperating = false;
    }

    @Override
    public void run() {
        isOperating = true;
        try {
            while(isOperating){
                List<BufferedImage> currentImages = new ArrayList<>();
                currentPipeline = nextPipeline;
                setPreviewPipelineAsNextPipeline();
                currentPipeline.executeOperations(currentImages);
                timeOfLastExecution = LocalTime.now();
            }
            LOGGER.info("Operation execution stopped.");
        } catch (StopExecutionException e) {
            LOGGER.error("Operation execution terminated!");
        }
    }
}
