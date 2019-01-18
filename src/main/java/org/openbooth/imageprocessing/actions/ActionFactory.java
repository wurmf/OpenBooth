package org.openbooth.imageprocessing.actions;

import org.openbooth.imageprocessing.exception.ProcessingException;

/**
 * A class based on this interface returns an Action based on the current state of the program
 * factories should not be singletons (use @Scope(BeanDefinition.SCOPE_PROTOTYPE))
 */

public interface ActionFactory {

    Action getAction() throws ProcessingException;
}
