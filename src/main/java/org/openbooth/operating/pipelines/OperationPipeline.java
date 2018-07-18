package org.openbooth.operating.pipelines;

import org.openbooth.operating.exception.StopExecutionException;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;

public interface OperationPipeline {

    void executeOperations(List<BufferedImage> images) throws StopExecutionException;

}
