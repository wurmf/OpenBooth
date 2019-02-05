package org.openbooth.imageprocessing.execution.pipelines.impl;

import org.openbooth.imageprocessing.actions.ActionFactory;
import org.openbooth.imageprocessing.actions.impl.ShowCountdownAcFac;
import org.openbooth.imageprocessing.exception.StopExecutionException;
import org.openbooth.imageprocessing.execution.executables.*;
import org.openbooth.imageprocessing.execution.executables.impl.ActionExecutable;
import org.openbooth.imageprocessing.execution.executables.impl.ImageProcessingExecutable;
import org.openbooth.imageprocessing.execution.executor.Executor;
import org.openbooth.imageprocessing.execution.pipelines.Pipeline;
import org.openbooth.imageprocessing.processors.ImageProcessorFactory;
import org.openbooth.imageprocessing.producer.camera.MakeShotsProcFac;
import org.openbooth.imageprocessing.consumer.gui.ShowShotProcFac;
import org.openbooth.imageprocessing.processors.storage.SaveImagesToShootingFolderProcFac;

import org.openbooth.storage.exception.StorageException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class ShotPipeline implements Pipeline {

    private List<Executable> pipeline;

    public ShotPipeline() throws StorageException{

        ActionExecutable preActions = createPreActions();
        ImageProcessingExecutable imageProcessing = createImageProcessing();

        pipeline = Arrays.asList(preActions, imageProcessing);
    }

    @Override
    public void runWith(Executor executor) throws StopExecutionException {
        executor.execute(pipeline);
    }

    private ActionExecutable createPreActions() throws StorageException{
        List<ActionFactory> actionFactories = Collections.singletonList(new ShowCountdownAcFac());

        return new ActionExecutable(actionFactories);
    }

    private ImageProcessingExecutable createImageProcessing() throws StorageException {

        List<ImageProcessorFactory> processorFactories = Collections.singletonList(new SaveImagesToShootingFolderProcFac());

        return new ImageProcessingExecutable(new MakeShotsProcFac(), processorFactories, new ShowShotProcFac());
    }



}
