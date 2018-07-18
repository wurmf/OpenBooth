package org.openbooth.operating.pipelines;

import org.openbooth.operating.exception.StopExecutionException;

import java.awt.image.BufferedImage;

public interface OperationPipeline {

    BufferedImage executeOperations(BufferedImage image) throws StopExecutionException;

}
