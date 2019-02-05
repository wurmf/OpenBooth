package org.openbooth.imageprocessing.execution.executables.impl;

import org.openbooth.imageprocessing.consumer.ImageConsumerFactory;
import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.execution.executables.Executable;
import org.openbooth.imageprocessing.processors.ImageProcessorFactory;
import org.openbooth.imageprocessing.producer.ImageProducerFactory;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;

public class ImageProcessingExecutable implements Executable {

    private ImageProducerFactory producerFactory;
    private List<ImageProcessorFactory> processorFactories;
    private ImageConsumerFactory consumerFactory;

    public ImageProcessingExecutable(ImageProducerFactory producerFactory, List<ImageProcessorFactory> processorFactories, ImageConsumerFactory consumerFactory) {
        this.producerFactory = producerFactory;
        this.processorFactories = processorFactories;
        this.consumerFactory = consumerFactory;
    }

    public ImageProcessingExecutable(ImageProducerFactory producerFactory, ImageConsumerFactory consumerFactory){
        this.producerFactory = producerFactory;
        this.processorFactories = Collections.emptyList();
        this.consumerFactory = consumerFactory;
    }


    @Override
    public void execute() throws ProcessingException {
            BufferedImage image = producerFactory.getProducer().produce();

            for(ImageProcessorFactory imageProcessorFactory : processorFactories){
                image = imageProcessorFactory.getProcessor().process(image);
            }

            consumerFactory.getConsumer().consume(image);
    }

}
