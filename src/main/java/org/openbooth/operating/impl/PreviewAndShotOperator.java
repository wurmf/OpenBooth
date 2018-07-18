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
import java.util.ArrayList;
import java.util.List;

@Component
public class PreviewAndShotOperator implements Operator {

    private static final Logger LOGGER  = LoggerFactory.getLogger(PreviewAndShotOperator.class);

    private OperationPipeline previewOperationPipeline;
    private OperationPipeline shotOperationsPipeline;


    private boolean triggered = false;
    private volatile boolean isOperating = false;

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
                List<BufferedImage> currentImages = new ArrayList<>();
                if(triggered){
                    shotOperationsPipeline.executeOperations(currentImages);
                    triggered = false;
                }else {
                    previewOperationPipeline.executeOperations(currentImages);
                }
            }
        } catch (StopExecutionException e) {
            LOGGER.error("Operation execution terminated!");
        }
        LOGGER.info("Operation execution stopped.");
    }
}
