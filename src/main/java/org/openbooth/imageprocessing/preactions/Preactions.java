package org.openbooth.imageprocessing.preactions;

import org.openbooth.imageprocessing.exception.ProcessingException;

public interface Preactions {

    void execute() throws ProcessingException;
}
