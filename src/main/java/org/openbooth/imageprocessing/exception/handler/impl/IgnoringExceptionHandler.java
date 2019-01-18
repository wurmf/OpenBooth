package org.openbooth.imageprocessing.exception.handler.impl;

import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.exception.handler.ProcessingExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class IgnoringExceptionHandler implements ProcessingExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(IgnoringExceptionHandler.class);

    @Override
    public void handleProcessingException(ProcessingException e) {
        LOGGER.debug("Ignored the following exception: ", e);
    }
}
