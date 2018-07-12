package org.openbooth.operating;

import org.openbooth.operating.exceptions.OperationExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.util.List;

@Component
public class OperatorImpl implements Operator{

    private static final Logger LOGGER  = LoggerFactory.getLogger(OperatorImpl.class);

    List<OperationFactory> defaultOperationFactories;
    List<OperationFactory> triggeredOperationFactories;

    private boolean triggered = false;
    private boolean errorCondition = false;

    private BufferedImage currentImage = null;

    @Override
    public void setDefaultOperationFactories(List<OperationFactory> defaultOperationFactories) {
        this.defaultOperationFactories = defaultOperationFactories;
    }

    @Override
    public void setTriggeredOperationFactories(List<OperationFactory> triggeredOperationFactories) {
        this.triggeredOperationFactories = triggeredOperationFactories;
    }

    @Override
    public void trigger() {
        triggered = true;
    }

    @Override
    public void run() {
        while(!errorCondition){
            if(triggered){
                errorCondition = !executeTriggerOperations();
                triggered = false;
            }else {
                errorCondition = !executeDefaultOperations();
            }
        }
    }

    private boolean executeDefaultOperations() {
        try {
            for(OperationFactory operationFactory: defaultOperationFactories){
                currentImage = operationFactory.getOperation().execute(currentImage);
            }
        } catch (OperationExecutionException e) {
            LOGGER.error("error during execution of default operations", e);
            return false;
        }
        return true;
    }

    private boolean executeTriggerOperations() {
        try {
            for(OperationFactory operationFactory : triggeredOperationFactories){
                currentImage = operationFactory.getOperation().execute(currentImage);
            }
        } catch (OperationExecutionException e) {
            LOGGER.error("error during execution of trigger operations", e);
            return false;
        }
        return true;
    }
}
