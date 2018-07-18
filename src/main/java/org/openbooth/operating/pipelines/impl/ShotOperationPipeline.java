package org.openbooth.operating.pipelines.impl;

import org.openbooth.operating.camera.MakeShotsOperationFactory;
import org.openbooth.operating.exception.handler.impl.StrictOperationExecutionExceptionHandler;
import org.openbooth.operating.gui.ShowShotOperationFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ShotOperationPipeline extends AbstractOperationPipeline {

    @Autowired
    public ShotOperationPipeline(ApplicationContext applicationContext){
        operationFactories = new ArrayList<>();
        operationFactories.add(applicationContext.getBean(MakeShotsOperationFactory.class));
        operationFactories.add(applicationContext.getBean(ShowShotOperationFactory.class));

        exceptionHandler = applicationContext.getBean(StrictOperationExecutionExceptionHandler.class);


    }

}
