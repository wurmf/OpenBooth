package org.openbooth.imageprocessing.processors.mirroring;

import org.openbooth.imageprocessing.processors.ImageProcessor;
import org.openbooth.imageprocessing.processors.ImageProcessorFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MirrorImageProcFac implements ImageProcessorFactory {

    @Override
    public ImageProcessor getProcessor() {
        return new MirrorImageProcessor();
    }
}
