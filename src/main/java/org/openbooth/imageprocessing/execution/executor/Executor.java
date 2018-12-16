package org.openbooth.imageprocessing.execution.executor;

import org.openbooth.imageprocessing.exception.StopExecutionException;
import org.openbooth.imageprocessing.execution.executables.Executable;

import java.util.List;

public interface Executor {

    void execute(List<Executable> executables) throws StopExecutionException;
}
