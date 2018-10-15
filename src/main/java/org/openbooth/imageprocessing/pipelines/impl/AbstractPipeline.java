package org.openbooth.imageprocessing.pipelines.impl;

import org.openbooth.imageprocessing.processors.ImageProcessorFactory;
import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.exception.StopExecutionException;
import org.openbooth.imageprocessing.pipelines.ImageProcessorPipeline;
import org.openbooth.imageprocessing.exception.handler.ProcessingExceptionHandler;

import java.awt.image.BufferedImage;
import java.util.List;

abstract class AbstractPipeline implements ImageProcessorPipeline {

    List<ImageProcessorFactory> processorFactories;
    ProcessingExceptionHandler exceptionHandler;


    @Override
    public void startProcessing(List<BufferedImage> images) throws StopExecutionException {
        try {
            for(ImageProcessorFactory imageProcessorFactory : processorFactories){
                imageProcessorFactory.getProcessor().process(images);
            }
        } catch (ProcessingException e) {
            exceptionHandler.handleProcessingException(e);
        }
    }
}
