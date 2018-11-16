package org.openbooth.imageprocessing.execution.executables.impl;

import org.openbooth.imageprocessing.actions.ActionFactory;
import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.execution.executables.Executable;

import java.util.List;

public class ActionExecutable implements Executable {

    private List<ActionFactory> actionFactories;

    public ActionExecutable(List<ActionFactory> actionFactories){
        this.actionFactories = actionFactories;
    }

    @Override
    public void execute() throws ProcessingException {
        for(ActionFactory actionFactory : actionFactories){
            actionFactory.getAction().execute();
        }
    }

}
