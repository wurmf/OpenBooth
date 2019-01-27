package org.openbooth.imageprocessing.execution.pipelines.impl;

import org.openbooth.imageprocessing.consumer.ImageConsumerFactory;
import org.openbooth.imageprocessing.exception.StopExecutionException;
import org.openbooth.imageprocessing.execution.executables.Executable;
import org.openbooth.imageprocessing.execution.executor.Executor;
import org.openbooth.imageprocessing.execution.executables.impl.ImageProcessingExecutable;
import org.openbooth.imageprocessing.execution.pipelines.Pipeline;
import org.openbooth.imageprocessing.processors.ImageProcessorFactory;
import org.openbooth.imageprocessing.processors.mirroring.MirrorImageProcFac;
import org.openbooth.imageprocessing.producer.ImageProducerFactory;
import org.openbooth.imageprocessing.producer.camera.MakePreviewProcFac;
import org.openbooth.imageprocessing.consumer.gui.ShowPreviewProcFac;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PreviewPipeline implements Pipeline {

    private List<Executable> pipeline;

    @Autowired
    public PreviewPipeline(ApplicationContext applicationContext){


        ImageProcessingExecutable imageProcessingExecutable = createImageProcessing(applicationContext);


        pipeline = Collections.singletonList(imageProcessingExecutable);

    }

    @Override
    public void runWith(Executor executor) throws StopExecutionException {
        executor.execute(pipeline);
    }

    private ImageProcessingExecutable createImageProcessing(ApplicationContext applicationContext){
        ImageProducerFactory producerFactory = applicationContext.getBean(MakePreviewProcFac.class);

        List<ImageProcessorFactory> processorFactories = new ArrayList<>();
        processorFactories.add(applicationContext.getBean(MirrorImageProcFac.class));

        ImageConsumerFactory consumerFactory = applicationContext.getBean(ShowPreviewProcFac.class);

        return new ImageProcessingExecutable(producerFactory, processorFactories, consumerFactory);
    }
}
