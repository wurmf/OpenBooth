package org.openbooth.operating.pipelines.impl;

import org.openbooth.operating.camera.MakePreviewOperationFactory;
import org.openbooth.operating.exception.handler.impl.StrictOperationExceptionHandler;
import org.openbooth.operating.gui.ShowPreviewOperationFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class PreviewOperationPipeline extends AbstractOperationPipeline {


    @Autowired
    public PreviewOperationPipeline(ApplicationContext applicationContext){
        operationFactories = new ArrayList<>();
        operationFactories.add(applicationContext.getBean(MakePreviewOperationFactory.class));
        operationFactories.add(applicationContext.getBean(ShowPreviewOperationFactory.class));


        exceptionHandler = applicationContext.getBean(StrictOperationExceptionHandler.class);
    }


}
