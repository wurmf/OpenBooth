package org.openbooth.imageprocessing.actions.impl;

import org.openbooth.gui.ShotFrameController;
import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.actions.Action;
import org.openbooth.imageprocessing.exception.StopExecutionException;
import org.openbooth.imageprocessing.execution.pipelines.impl.PreviewPipeline;

public class ShowCountdownAction implements Action {
    private ShotFrameController shotFrameController;
    private int counter;
    private PreviewPipeline previewPipeline;

    ShowCountdownAction(ShotFrameController shotFrameController, PreviewPipeline previewPipeline, int counter) {
        this.counter = counter;
        this.shotFrameController = shotFrameController;
        this.previewPipeline = previewPipeline;
    }

    @Override
    public void execute() throws ProcessingException {
        shotFrameController.startTimer(counter);
        while (!shotFrameController.isCountDownFinished()){
            try {
                previewPipeline.run();
            } catch (StopExecutionException e) {
                throw new ProcessingException("Error while showing countdown",e);
            }
        }

    }
}
