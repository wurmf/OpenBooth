package org.openbooth.imageprocessing.execution.executables.impl;

import org.openbooth.imageprocessing.exception.OperationCreationException;
import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.execution.executables.Executable;
import org.openbooth.imageprocessing.operations.OperationFactory;
import org.openbooth.imageprocessing.operations.OperationFactoryProvider;
import org.openbooth.imageprocessing.operations.consumers.ImageConsumer;
import org.openbooth.imageprocessing.operations.consumers.ImageConsumingOperation;
import org.openbooth.imageprocessing.operations.processors.ImageProcessingOperation;
import org.openbooth.imageprocessing.operations.processors.ImageProcessor;
import org.openbooth.imageprocessing.operations.producers.ImageProducer;
import org.openbooth.imageprocessing.operations.producers.ImageProducingOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class ImageProcessingExecutable implements Executable {

    private OperationFactory<ImageProducer> producerFactory;
    private List<OperationFactory<ImageProcessor>> processorFactories;
    private OperationFactory<ImageConsumer> consumerFactory;

    private OperationFactoryProvider<ImageProducer, ImageProducingOperation> producerFactoryProvider;
    private OperationFactoryProvider<ImageProcessor, ImageProcessingOperation> processorFactoryProvider;
    private OperationFactoryProvider<ImageConsumer, ImageConsumingOperation> consumerFactoryProvider;



    @Autowired
    private ImageProcessingExecutable(
            OperationFactoryProvider<ImageConsumer, ImageConsumingOperation> consumerFactoryProvider,
            OperationFactoryProvider<ImageProcessor, ImageProcessingOperation> processorFactoryProvider,
            OperationFactoryProvider<ImageProducer, ImageProducingOperation> producerFactoryProvider
    ){
        this.producerFactoryProvider = producerFactoryProvider;
        this.processorFactoryProvider = processorFactoryProvider;
        this.consumerFactoryProvider = consumerFactoryProvider;

    }

    public void setOperations(ImageProducingOperation producingOperation, List<ImageProcessingOperation> processingOperations, ImageConsumingOperation consumingOperation) throws OperationCreationException{
        this.producerFactory = producerFactoryProvider.getOperationFactory(producingOperation);

        this.processorFactories = new ArrayList<>();
        for (ImageProcessingOperation processingOperation : processingOperations) {
            processorFactories.add(processorFactoryProvider.getOperationFactory(processingOperation));
        }

        this.consumerFactory = consumerFactoryProvider.getOperationFactory(consumingOperation);


    }


    @Override
    public void execute() throws ProcessingException {

        BufferedImage image = producerFactory.getOperation().produce();

        for(OperationFactory<ImageProcessor> imageProcessorFactory : processorFactories){
            image = imageProcessorFactory.getOperation().process(image);
        }

        consumerFactory.getOperation().consume(image);
    }

}
