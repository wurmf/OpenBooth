package org.openbooth.imageprocessing.actions.impl;

import org.openbooth.gui.ShotFrameController;
import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.actions.Action;
import org.openbooth.imageprocessing.actions.ActionFactory;
import org.openbooth.storage.KeyValueStore;
import org.openbooth.storage.exception.KeyValueStoreException;
import org.springframework.stereotype.Component;

@Component
public class ShowCountdownAcFac implements ActionFactory {
    private ShotFrameController shotFrameController;

    private KeyValueStore keyValueStore;

    public ShowCountdownAcFac(ShotFrameController shotFrameController, KeyValueStore keyValueStore){
        this.shotFrameController = shotFrameController;
        this.keyValueStore = keyValueStore;
    }

    @Override
    public Action getCounter() throws ProcessingException {
        try {
            int counter = keyValueStore.getInt(KeyValueStore.COUNTER_PER_SHOT);
            return new ShowCountdownAction(shotFrameController, counter);
        } catch (KeyValueStoreException e) {
            throw new ProcessingException(e);
        }
    }
}
