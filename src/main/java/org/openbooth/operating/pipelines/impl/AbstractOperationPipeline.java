package org.openbooth.operating.pipelines.impl;

import org.openbooth.operating.OperationFactory;
import org.openbooth.operating.exception.OperationExecutionException;
import org.openbooth.operating.exception.StopExecutionException;
import org.openbooth.operating.pipelines.OperationPipeline;
import org.openbooth.operating.exception.handler.OperationExecutionExceptionHandler;

import java.awt.image.BufferedImage;
import java.util.List;

abstract class AbstractOperationPipeline implements OperationPipeline {

    List<OperationFactory> operationFactories;
    OperationExecutionExceptionHandler exceptionHandler;


    @Override
    public BufferedImage executeOperations(BufferedImage image) throws StopExecutionException {
        BufferedImage currentImage = image;
        try {
            for(OperationFactory operationFactory: operationFactories){
                currentImage = operationFactory.getOperation().execute(currentImage);
            }
            return currentImage;
        } catch (OperationExecutionException e) {
            exceptionHandler.handleOperationExecutionException(e);
        }
        return image;
    }
}
