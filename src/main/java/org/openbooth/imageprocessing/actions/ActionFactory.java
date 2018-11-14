package org.openbooth.imageprocessing.actions;

import org.openbooth.imageprocessing.exception.ProcessingException;

public interface ActionFactory {

    Action getCounter() throws ProcessingException;
}
