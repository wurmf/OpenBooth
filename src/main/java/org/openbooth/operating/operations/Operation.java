package org.openbooth.operating.operations;

import org.openbooth.operating.exception.OperationException;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * This interface is used for any operation dealing with images,
 * the images in the list might replaced by new images when executed
 * or any other operation using the images is performed
 */
public interface Operation {

    void execute(List<BufferedImage> images) throws OperationException;

}
