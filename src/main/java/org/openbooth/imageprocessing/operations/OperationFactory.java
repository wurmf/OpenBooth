package org.openbooth.imageprocessing.operations;

import org.openbooth.imageprocessing.exception.ProcessingException;

public interface OperationFactory<O> {

    O getOperation() throws ProcessingException;

}
