package org.openbooth.imageprocessing.preactions.impl;

import org.openbooth.gui.ShotFrameController;
import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.preactions.Preactions;

public class ShowPreactionProcessor implements Preactions {
    ShotFrameController shotFrameController;
    Integer counter;
    public ShowPreactionProcessor(ShotFrameController shotFrameController, int counter) {
        this.counter = counter;
        this.shotFrameController = shotFrameController;
    }

    @Override
    public void execute() throws ProcessingException {
        shotFrameController.startTimer(counter);
        try {
            Thread.sleep(counter*1000);
        } catch (InterruptedException e) {
            throw new ProcessingException(e);
        }

    }
}
