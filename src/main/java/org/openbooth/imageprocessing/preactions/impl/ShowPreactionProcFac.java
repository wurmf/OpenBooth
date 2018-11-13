package org.openbooth.imageprocessing.preactions.impl;

import org.openbooth.gui.ShotFrameController;
import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.preactions.Preactions;
import org.openbooth.imageprocessing.preactions.PreactionsFac;
import org.openbooth.storage.KeyValueStore;
import org.openbooth.storage.exception.KeyValueStoreException;
import org.springframework.stereotype.Component;

@Component
public class ShowPreactionProcFac implements PreactionsFac {
    private ShotFrameController shotFrameController;

    private KeyValueStore keyValueStore;

    public ShowPreactionProcFac(ShotFrameController shotFrameController, KeyValueStore keyValueStore){
        this.shotFrameController = shotFrameController;
        this.keyValueStore = keyValueStore;
    }

    @Override
    public Preactions getCounter() throws ProcessingException {
        try {
            int counter = keyValueStore.getInt(KeyValueStore.COUNTER_PER_SHOT);
            return new ShowPreactionProcessor(shotFrameController, counter);
        } catch (KeyValueStoreException e) {
            throw new ProcessingException(e);
        }
    }
}
