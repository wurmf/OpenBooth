package org.openbooth.imageprocessing.execution.pipelines.impl;

import org.openbooth.imageprocessing.consumer.ImageConsumerFactory;
import org.openbooth.imageprocessing.exception.StopExecutionException;
import org.openbooth.imageprocessing.exception.handler.ProcessingExceptionHandler;
import org.openbooth.imageprocessing.execution.executables.Executor;
import org.openbooth.imageprocessing.execution.executables.impl.ImageProcessingExecutable;
import org.openbooth.imageprocessing.execution.executables.impl.StandardExecutor;
import org.openbooth.imageprocessing.execution.pipelines.Pipeline;
import org.openbooth.imageprocessing.producer.ImageProducerFactory;
import org.openbooth.imageprocessing.producer.camera.MakePreviewProcFac;
import org.openbooth.imageprocessing.exception.handler.impl.StrictProcessingExceptionHandler;
import org.openbooth.imageprocessing.consumer.gui.ShowPreviewProcFac;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class PreviewPipeline implements Pipeline {

    private Executor executor;

    @Autowired
    public PreviewPipeline(ApplicationContext applicationContext){


        ImageProcessingExecutable imageProcessingExecutable = createImageProcessing(applicationContext);

        ProcessingExceptionHandler exceptionHandler = applicationContext.getBean(StrictProcessingExceptionHandler.class);

        executor = new StandardExecutor(Collections.singletonList(imageProcessingExecutable), exceptionHandler);
    }

    @Override
    public void run() throws StopExecutionException {
        executor.execute();
    }

    private ImageProcessingExecutable createImageProcessing(ApplicationContext applicationContext){
        ImageProducerFactory producerFactory = applicationContext.getBean(MakePreviewProcFac.class);
        ImageConsumerFactory consumerFactory = applicationContext.getBean(ShowPreviewProcFac.class);

        return new ImageProcessingExecutable(producerFactory, Collections.emptyList(), consumerFactory);
    }
}
