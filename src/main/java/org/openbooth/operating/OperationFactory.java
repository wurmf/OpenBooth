package org.openbooth.operating;

import org.openbooth.operating.exception.OperationException;
import org.openbooth.operating.operations.Operation;

/**
 * A class implementing this interface returns an Operation based on it's current status
 */
public interface OperationFactory {

    Operation getOperation() throws OperationException;
}
