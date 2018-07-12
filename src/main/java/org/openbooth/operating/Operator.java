package org.openbooth.operating;

import java.util.List;

/**
 * A class implementing the Operator interface executes two streams of Operations:
 * one stream is executed per default in a loop and the other one is triggered
 * After executing the second stream of Operations the Operator starts again with the first stream
 */
public interface Operator extends Runnable{

    void setDefaultOperationFactories(List<OperationFactory> defaultOperationFactories);

    void setTriggeredOperationFactories(List<OperationFactory> triggeredOperationFactories);

    void trigger();
}
