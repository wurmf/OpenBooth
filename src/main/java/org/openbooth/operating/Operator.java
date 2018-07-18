package org.openbooth.operating;

import org.openbooth.operating.pipelines.OperationPipeline;

/**
 * A class implementing the Operator interface executes two pipelines of operations:
 * one pipeline is executed per default in a loop and the other one is triggered
 * After executing the second pipeline of Operations the Operator starts again with the first one
 */
public interface Operator extends Runnable{

    void trigger();

    /***
     * Tells the operator to stop operating.
     */
    void stopOperating();
}
