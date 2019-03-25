package org.openbooth.imageprocessing.execution.pipelines.impl;

import org.openbooth.application.ApplicationContextProvider;
import org.openbooth.imageprocessing.exception.OperationCreationException;
import org.openbooth.imageprocessing.operations.actions.ActionOperation;
import org.openbooth.imageprocessing.exception.StopExecutionException;
import org.openbooth.imageprocessing.execution.executables.*;
import org.openbooth.imageprocessing.execution.executables.impl.ActionExecutable;
import org.openbooth.imageprocessing.execution.executables.impl.ImageProcessingExecutable;
import org.openbooth.imageprocessing.execution.executor.Executor;
import org.openbooth.imageprocessing.execution.pipelines.Pipeline;
import org.openbooth.imageprocessing.operations.consumers.ImageConsumingOperation;
import org.openbooth.imageprocessing.operations.processors.ImageProcessingOperation;
import org.openbooth.imageprocessing.operations.producers.ImageProducingOperation;

import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.openbooth.imageprocessing.operations.actions.ActionOperation.*;
import static org.openbooth.imageprocessing.operations.consumers.ImageConsumingOperation.*;
import static org.openbooth.imageprocessing.operations.processors.ImageProcessingOperation.*;
import static org.openbooth.imageprocessing.operations.producers.ImageProducingOperation.*;


public class ShotPipeline implements Pipeline {

    private static final List<ActionOperation> PRE_ACTIONS = Collections.singletonList(SHOW_COUNTDOWN);
    private static final ImageProducingOperation PRODUCER = MAKE_SHOT;
    private static final List<ImageProcessingOperation> PROCESSORS = Collections.singletonList(SAVE_IMAGE_TO_SHOOTING_FOLDER);
    private static final ImageConsumingOperation CONSUMER = SHOW_SHOT;


    private List<Executable> pipeline;

    public ShotPipeline() throws OperationCreationException{
       ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();

        ActionExecutable preActions = applicationContext.getBean(ActionExecutable.class);
        preActions.setActionOperations(PRE_ACTIONS);

        ImageProcessingExecutable imageProcessing = applicationContext.getBean(ImageProcessingExecutable.class);
        imageProcessing.setOperations(PRODUCER, PROCESSORS, CONSUMER);

        pipeline = Arrays.asList(preActions, imageProcessing);
    }

    @Override
    public void runWith(Executor executor) throws StopExecutionException {
        executor.execute(pipeline);
    }

}
