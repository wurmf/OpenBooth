package org.openbooth.operating.pipelines.impl;

import org.openbooth.operating.OperationFactory;
import org.openbooth.operating.exception.OperationException;
import org.openbooth.operating.exception.StopExecutionException;
import org.openbooth.operating.pipelines.OperationPipeline;
import org.openbooth.operating.exception.handler.OperationExceptionHandler;

import java.awt.image.BufferedImage;
import java.util.List;

abstract class AbstractOperationPipeline implements OperationPipeline {

    List<OperationFactory> operationFactories;
    OperationExceptionHandler exceptionHandler;


    @Override
    public void executeOperations(List<BufferedImage> images) throws StopExecutionException {
        try {
            for(OperationFactory operationFactory: operationFactories){
                operationFactory.getOperation().execute(images);
            }
        } catch (OperationException e) {
            exceptionHandler.handleOperationException(e);
        }
    }
}
