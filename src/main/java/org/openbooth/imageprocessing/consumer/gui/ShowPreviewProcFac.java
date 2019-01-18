package org.openbooth.imageprocessing.consumer.gui;

import org.openbooth.gui.ShotFrameController;
import org.openbooth.imageprocessing.consumer.ImageConsumer;
import org.openbooth.imageprocessing.consumer.ImageConsumerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ShowPreviewProcFac implements ImageConsumerFactory {

    private ShotFrameController shotFrameController;

    public ShowPreviewProcFac(ShotFrameController shotFrameController){
        this.shotFrameController = shotFrameController;
    }

    @Override
    public ImageConsumer getConsumer() {
        return new ShowPreviewProcessor(shotFrameController);
    }
}
