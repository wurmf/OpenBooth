package org.openbooth.imageprocessing.operations;

import org.openbooth.imageprocessing.exception.OperationCreationException;

public interface OperationFactoryProvider<T, O> {

    OperationFactory<T> getOperationFactory(O operation) throws OperationCreationException;

}
