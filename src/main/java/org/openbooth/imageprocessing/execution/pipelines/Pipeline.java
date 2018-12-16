package org.openbooth.imageprocessing.execution.pipelines;

import org.openbooth.imageprocessing.exception.StopExecutionException;
import org.openbooth.imageprocessing.execution.executor.Executor;

public interface Pipeline {

    void runWith(Executor executor) throws StopExecutionException;

}
