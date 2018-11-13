package org.openbooth.imageprocessing.preactions;

import org.openbooth.imageprocessing.exception.ProcessingException;

public interface PreactionsFac {

    Preactions getCounter() throws ProcessingException;
}
