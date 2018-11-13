package org.openbooth.imageprocessing.pipelines.impl;

import org.openbooth.imageprocessing.consumer.ImageConsumerFactory;
import org.openbooth.imageprocessing.actions.ActionFactory;
import org.openbooth.imageprocessing.processors.ImageProcessorFactory;
import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.exception.StopExecutionException;
import org.openbooth.imageprocessing.pipelines.ImageProcessorPipeline;
import org.openbooth.imageprocessing.exception.handler.ProcessingExceptionHandler;
import org.openbooth.imageprocessing.producer.ImageProducerFactory;

import java.awt.image.BufferedImage;
import java.util.List;

abstract class AbstractPipeline implements ImageProcessorPipeline {

    List<ImageProcessorFactory> processorFactories;
    ProcessingExceptionHandler exceptionHandler;

    ImageConsumerFactory consumerFactory;
    ImageProducerFactory producerFactory;
    List <ActionFactory> preActionsFactories;

    @Override
    public void execute() throws StopExecutionException {
        try {

            for(ActionFactory actionFactory: preActionsFactories){
                actionFactory.getCounter().execute();
            }

            BufferedImage image = producerFactory.getProducer().produce();

            for(ImageProcessorFactory imageProcessorFactory : processorFactories){
               image = imageProcessorFactory.getProcessor().process(image);
            }

            consumerFactory.getConsumer().consume(image);


        } catch (ProcessingException e) {
            exceptionHandler.handleProcessingException(e);
        }
    }
}
