package org.openbooth.imageprocessing.execution.pipelines;

import org.openbooth.imageprocessing.exception.StopExecutionException;

public interface Pipeline {

    void run() throws StopExecutionException;

}
