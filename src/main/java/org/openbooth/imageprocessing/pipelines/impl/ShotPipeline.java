package org.openbooth.imageprocessing.pipelines.impl;

import org.openbooth.imageprocessing.processors.camera.MakeShotsProcFac;
import org.openbooth.imageprocessing.exception.handler.impl.StrictProcessingExceptionHandler;
import org.openbooth.imageprocessing.processors.gui.ShowShotProcFac;
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
        processorFactories.add(applicationContext.getBean(MakeShotsProcFac.class));
        processorFactories.add(applicationContext.getBean(ShowShotProcFac.class));
        processorFactories.add(applicationContext.getBean(SaveImagesToShootingFolderProcFac.class));

        exceptionHandler = applicationContext.getBean(StrictProcessingExceptionHandler.class);


    }

}
