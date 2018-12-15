package org.openbooth.imageprocessing.actions.impl;

import org.openbooth.config.key.ConfigBooleanKeys;
import org.openbooth.config.key.ConfigIntegerKeys;
import org.openbooth.gui.ShotFrameController;
import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.actions.Action;
import org.openbooth.imageprocessing.actions.ActionFactory;
import org.openbooth.imageprocessing.execution.pipelines.impl.PreviewPipeline;
import org.openbooth.service.InformationDistributor;
import org.openbooth.storage.KeyValueStore;
import org.openbooth.storage.exception.KeyValueStoreException;
import org.springframework.stereotype.Component;

@Component
public class ShowCountdownAcFac implements ActionFactory {
    private ShotFrameController shotFrameController;

    private KeyValueStore keyValueStore;
    private InformationDistributor informationDistributor;

    private PreviewPipeline previewPipeline;

    public ShowCountdownAcFac(ShotFrameController shotFrameController, KeyValueStore keyValueStore, PreviewPipeline previewPipeline, InformationDistributor informationDistributor){
        this.shotFrameController = shotFrameController;
        this.keyValueStore = keyValueStore;
        this.informationDistributor = informationDistributor;
        this.previewPipeline = previewPipeline;
    }

    @Override
    public Action getAction() throws ProcessingException {
        try {
            if(!informationDistributor.isTimedShotEnabled()) return new EmptyAction();

            int counter = keyValueStore.getInt(ConfigIntegerKeys.SHOT_COUNTDOWN.key);
            return new ShowCountdownAction(shotFrameController, previewPipeline, counter);
        } catch (KeyValueStoreException e) {
            throw new ProcessingException(e);
        }
    }
}
