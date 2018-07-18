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

@Component
public class PreviewAndShotOperator implements Operator {

    private static final Logger LOGGER  = LoggerFactory.getLogger(PreviewAndShotOperator.class);

    private OperationPipeline previewOperationPipeline;
    private OperationPipeline shotOperationsPipeline;


    private boolean triggered = false;
    private volatile boolean isOperating = false;
    private BufferedImage currentImage = null;

    @Autowired
    public PreviewAndShotOperator(PreviewOperationPipeline previewOperationPipeline, ShotOperationPipeline shotOperationPipeline){
        this.previewOperationPipeline = previewOperationPipeline;
        this.shotOperationsPipeline = shotOperationPipeline;
    }


    @Override
    public void trigger() {
        triggered = true;
    }

    @Override
    public void stopOperating(){
        isOperating = false;
    }

    @Override
    public void run() {
        isOperating = true;
        try {
            while(isOperating){
                if(triggered){
                    currentImage = shotOperationsPipeline.executeOperations(currentImage);
                    triggered = false;
                }else {
                    currentImage = previewOperationPipeline.executeOperations(currentImage);
                }
            }
        } catch (StopExecutionException e) {
            LOGGER.error("Operation execution terminated!");
        }
        LOGGER.info("Operation execution stopped.");
    }
}
