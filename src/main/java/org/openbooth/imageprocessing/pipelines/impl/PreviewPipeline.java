package org.openbooth.imageprocessing.pipelines.impl;

import org.openbooth.imageprocessing.processors.camera.MakePreviewProcFac;
import org.openbooth.imageprocessing.exception.handler.impl.StrictProcessingExceptionHandler;
import org.openbooth.imageprocessing.processors.gui.ShowPreviewProcFac;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class PreviewPipeline extends AbstractPipeline {


    @Autowired
    public PreviewPipeline(ApplicationContext applicationContext){
        processorFactories = new ArrayList<>();
        processorFactories.add(applicationContext.getBean(MakePreviewProcFac.class));
        processorFactories.add(applicationContext.getBean(ShowPreviewProcFac.class));


        exceptionHandler = applicationContext.getBean(StrictProcessingExceptionHandler.class);
    }


}
