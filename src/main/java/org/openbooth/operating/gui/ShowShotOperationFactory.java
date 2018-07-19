package org.openbooth.operating.gui;

import org.openbooth.gui.ShotFrameController;
import org.openbooth.operating.OperationFactory;
import org.openbooth.operating.exception.OperationException;
import org.openbooth.operating.operations.Operation;
import org.openbooth.storage.KeyValueStore;
import org.openbooth.storage.exception.PersistenceException;
import org.springframework.stereotype.Component;

@Component
public class ShowShotOperationFactory implements OperationFactory {

    private ShotFrameController shotFrameController;
    private KeyValueStore keyValueStore;

    public ShowShotOperationFactory(ShotFrameController shotFrameController, KeyValueStore keyValueStore){
        this.shotFrameController = shotFrameController;
        this.keyValueStore = keyValueStore;
    }

    @Override
    public Operation getOperation() throws OperationException {
        try {
            int showShotTime = keyValueStore.getInt("show_shot_time");
            return new ShowShotsOperation(shotFrameController, showShotTime);
        } catch (PersistenceException e) {
            throw new OperationException(e);
        }
    }
}
