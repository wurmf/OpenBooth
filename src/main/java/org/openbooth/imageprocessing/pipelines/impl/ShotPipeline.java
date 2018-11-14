package org.openbooth.imageprocessing.pipelines.impl;

import org.openbooth.imageprocessing.actions.impl.ShowCountdownAcFac;
import org.openbooth.imageprocessing.producer.camera.MakeShotsProcFac;
import org.openbooth.imageprocessing.exception.handler.impl.StrictProcessingExceptionHandler;
import org.openbooth.imageprocessing.consumer.gui.ShowShotProcFac;
import org.openbooth.imageprocessing.processors.storage.SaveImagesToShootingFolderProcFac;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ShotPipeline extends AbstractPipeline {

    @Autowired
    public ShotPipeline(ApplicationContext applicationContext){
        processorFactories = new ArrayList<>();
        processorFactories.add(applicationContext.getBean(SaveImagesToShootingFolderProcFac.class));

        preActionsFactories = new ArrayList<>();
        preActionsFactories.add(applicationContext.getBean(ShowCountdownAcFac.class));

        producerFactory = applicationContext.getBean(MakeShotsProcFac.class);
        consumerFactory = applicationContext.getBean(ShowShotProcFac.class);

        exceptionHandler = applicationContext.getBean(StrictProcessingExceptionHandler.class);


    }

}
