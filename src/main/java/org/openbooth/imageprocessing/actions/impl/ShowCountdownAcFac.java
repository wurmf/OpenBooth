package org.openbooth.imageprocessing.actions.impl;

import org.openbooth.gui.ShotFrameController;
import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.actions.Action;
import org.openbooth.imageprocessing.actions.ActionFactory;
import org.openbooth.imageprocessing.pipelines.impl.PreviewPipeline;
import org.openbooth.storage.KeyValueStore;
import org.openbooth.storage.exception.KeyValueStoreException;
import org.springframework.stereotype.Component;

@Component
public class ShowCountdownAcFac implements ActionFactory {
    private ShotFrameController shotFrameController;

    private KeyValueStore keyValueStore;

    private PreviewPipeline previewPipeline;

    public ShowCountdownAcFac(ShotFrameController shotFrameController, KeyValueStore keyValueStore, PreviewPipeline previewPipeline){
        this.shotFrameController = shotFrameController;
        this.keyValueStore = keyValueStore;
        this.previewPipeline = previewPipeline;
    }

    @Override
    public Action getCounter() throws ProcessingException {
        try {
            int counter = keyValueStore.getInt(KeyValueStore.COUNTER_PER_SHOT);
            return new ShowCountdownAction(shotFrameController, previewPipeline, counter);
        } catch (KeyValueStoreException e) {
            throw new ProcessingException(e);
        }
    }
}
