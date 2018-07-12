package org.openbooth.operating;

import org.openbooth.operating.camera.MakePreviewOperationFactory;
import org.openbooth.operating.gui.ShowPreviewOperationFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OperationsManager {

    private ApplicationContext applicationContext;
    private Operator operator;
    private Thread executingThread;

    public OperationsManager(Operator operator, ApplicationContext applicationContext){
        this.operator = operator;
        this.applicationContext = applicationContext;
    }

    public void startExecution(){
        List<OperationFactory> defaultOperationFactories = new ArrayList<>();
        defaultOperationFactories.add(applicationContext.getBean(MakePreviewOperationFactory.class));
        defaultOperationFactories.add(applicationContext.getBean(ShowPreviewOperationFactory.class));
        operator.setDefaultOperationFactories(defaultOperationFactories);
        operator.setTriggeredOperationFactories(new ArrayList());

        executingThread = new Thread(operator);
        executingThread.start();
    }
}
