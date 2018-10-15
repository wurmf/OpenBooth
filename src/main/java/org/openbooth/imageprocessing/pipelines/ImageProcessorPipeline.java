package org.openbooth.imageprocessing.pipelines;

import org.openbooth.imageprocessing.exception.StopExecutionException;

import java.awt.image.BufferedImage;
import java.util.List;

public interface ImageProcessorPipeline {

    void execute() throws StopExecutionException;

}
