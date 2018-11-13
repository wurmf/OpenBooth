package org.openbooth.imageprocessing.actions.impl;

import org.openbooth.gui.ShotFrameController;
import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.actions.Action;

public class ShowCountdownAction implements Action {
    private ShotFrameController shotFrameController;
    private int counter;

    ShowCountdownAction(ShotFrameController shotFrameController, int counter) {
        this.counter = counter;
        this.shotFrameController = shotFrameController;
    }

    @Override
    public void execute() throws ProcessingException {
        shotFrameController.startTimer(counter);
        try {
            Thread.sleep(counter * 1000L);
        } catch (InterruptedException e) {
            throw new ProcessingException(e);
        }

    }
}
