package org.openbooth.imageprocessing.processors.mirroring;

import org.openbooth.imageprocessing.processors.ImageProcessor;
import org.openbooth.imageprocessing.processors.ImageProcessorFactory;


public class MirrorImageProcFac implements ImageProcessorFactory {

    @Override
    public ImageProcessor getProcessor() {
        return new MirrorImageProcessor();
    }
}
