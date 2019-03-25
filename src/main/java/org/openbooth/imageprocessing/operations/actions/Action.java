package org.openbooth.imageprocessing.operations.actions;

import org.openbooth.imageprocessing.exception.ProcessingException;

public interface Action {

    void execute() throws ProcessingException;
}
