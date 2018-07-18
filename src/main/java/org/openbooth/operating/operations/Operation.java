package org.openbooth.operating.operations;

import org.openbooth.operating.exception.OperationExecutionException;

import java.awt.image.BufferedImage;

/**
 * This interface is used for any operation dealing with images,
 * it might produce a new image when executed or perform any other operation using the image
 */
public interface Operation {

    BufferedImage execute(BufferedImage image) throws OperationExecutionException;

}
