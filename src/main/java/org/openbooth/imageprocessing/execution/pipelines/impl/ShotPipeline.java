package org.openbooth.imageprocessing.execution.pipelines.impl;

import org.openbooth.imageprocessing.actions.ActionFactory;
import org.openbooth.imageprocessing.actions.impl.ShowCountdownAcFac;
import org.openbooth.imageprocessing.consumer.ImageConsumerFactory;
import org.openbooth.imageprocessing.exception.StopExecutionException;
import org.openbooth.imageprocessing.execution.executables.*;
import org.openbooth.imageprocessing.execution.executables.impl.ActionExecutable;
import org.openbooth.imageprocessing.execution.executables.impl.ImageProcessingExecutable;
import org.openbooth.imageprocessing.execution.executor.Executor;
import org.openbooth.imageprocessing.execution.pipelines.Pipeline;
import org.openbooth.imageprocessing.processors.ImageProcessorFactory;
import org.openbooth.imageprocessing.producer.ImageProducerFactory;
import org.openbooth.imageprocessing.producer.camera.MakeShotsProcFac;
import org.openbooth.imageprocessing.consumer.gui.ShowShotProcFac;
import org.openbooth.imageprocessing.processors.storage.SaveImagesToShootingFolderProcFac;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ShotPipeline implements Pipeline {

    private List<Executable> pipeline;

    @Autowired
    public ShotPipeline(ApplicationContext applicationContext){

        ActionExecutable preActions = createPreActions(applicationContext);
        ImageProcessingExecutable imageProcessing = createImageProcessing(applicationContext);

        pipeline = Arrays.asList(preActions, imageProcessing);
    }

    @Override
    public void runWith(Executor executor) throws StopExecutionException {
        executor.execute(pipeline);
    }

    private ActionExecutable createPreActions(ApplicationContext applicationContext){
        List<ActionFactory> actionFactories = new ArrayList<>();
        actionFactories.add(applicationContext.getBean(ShowCountdownAcFac.class));
        return new ActionExecutable(actionFactories);
    }

    private ImageProcessingExecutable createImageProcessing(ApplicationContext applicationContext){
        ImageProducerFactory producerFactory = applicationContext.getBean(MakeShotsProcFac.class);
        ImageConsumerFactory consumerFactory = applicationContext.getBean(ShowShotProcFac.class);

        List<ImageProcessorFactory> processorFactories = new ArrayList<>();
        processorFactories.add(applicationContext.getBean(SaveImagesToShootingFolderProcFac.class));

        return new ImageProcessingExecutable(producerFactory, processorFactories, consumerFactory);
    }



}
