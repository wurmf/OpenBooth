package org.openbooth.imageprocessing.execution.executables;

import org.openbooth.imageprocessing.exception.ProcessingException;

public interface Executable {

    void execute() throws ProcessingException;
}
