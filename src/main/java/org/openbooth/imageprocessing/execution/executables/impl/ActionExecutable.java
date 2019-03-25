package org.openbooth.imageprocessing.execution.executables.impl;

import org.openbooth.imageprocessing.exception.OperationCreationException;
import org.openbooth.imageprocessing.operations.OperationFactory;
import org.openbooth.imageprocessing.operations.OperationFactoryProvider;
import org.openbooth.imageprocessing.operations.actions.Action;
import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.execution.executables.Executable;
import org.openbooth.imageprocessing.operations.actions.ActionOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class ActionExecutable implements Executable {

    private OperationFactoryProvider<Action, ActionOperation> actionFactoryProvider;
    private List<OperationFactory<Action>> actionFactories;

    @Autowired
    private ActionExecutable(OperationFactoryProvider<Action, ActionOperation> actionFactoryProvider){
        this.actionFactoryProvider = actionFactoryProvider;
    }

    public void setActionOperations(List<ActionOperation> actionOperations) throws OperationCreationException {
        actionFactories = new ArrayList<>();
        for (ActionOperation actionOperation : actionOperations) {
            actionFactories.add(actionFactoryProvider.getOperationFactory(actionOperation));
        }
    }

    @Override
    public void execute() throws ProcessingException {
        for(OperationFactory<Action> actionFactory : actionFactories){
            actionFactory.getOperation().execute();
        }
    }

}
