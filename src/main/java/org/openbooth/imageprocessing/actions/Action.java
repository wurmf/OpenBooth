package org.openbooth.imageprocessing.actions;

import org.openbooth.imageprocessing.exception.ProcessingException;

public interface Action {

    void execute() throws ProcessingException;
}
