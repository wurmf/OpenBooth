package org.openbooth.operating.pipelines.impl;

import org.openbooth.operating.OperationFactory;
import org.openbooth.operating.exception.OperationExecutionException;
import org.openbooth.operating.exception.StopExecutionException;
import org.openbooth.operating.pipelines.OperationPipeline;
import org.openbooth.operating.exception.handler.OperationExecutionExceptionHandler;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;

abstract class AbstractOperationPipeline implements OperationPipeline {

    List<OperationFactory> operationFactories;
    OperationExecutionExceptionHandler exceptionHandler;


    @Override
    public void executeOperations(List<BufferedImage> images) throws StopExecutionException {
        try {
            for(OperationFactory operationFactory: operationFactories){
                operationFactory.getOperation().execute(images);
            }
        } catch (OperationExecutionException e) {
            exceptionHandler.handleOperationExecutionException(e);
        }
    }
}
