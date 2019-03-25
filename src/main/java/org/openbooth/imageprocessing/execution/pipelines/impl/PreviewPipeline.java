package org.openbooth.imageprocessing.execution.pipelines.impl;

import org.openbooth.application.ApplicationContextProvider;
import org.openbooth.imageprocessing.exception.OperationCreationException;
import org.openbooth.imageprocessing.exception.StopExecutionException;
import org.openbooth.imageprocessing.execution.executables.Executable;
import org.openbooth.imageprocessing.execution.executor.Executor;
import org.openbooth.imageprocessing.execution.executables.impl.ImageProcessingExecutable;
import org.openbooth.imageprocessing.execution.pipelines.Pipeline;
import org.openbooth.imageprocessing.operations.consumers.ImageConsumingOperation;
import org.openbooth.imageprocessing.operations.processors.ImageProcessingOperation;
import org.openbooth.imageprocessing.operations.producers.ImageProducingOperation;
import org.springframework.context.ApplicationContext;

import java.util.Collections;
import java.util.List;

import static org.openbooth.imageprocessing.operations.consumers.ImageConsumingOperation.SHOW_PREVIEW;
import static org.openbooth.imageprocessing.operations.processors.ImageProcessingOperation.*;
import static org.openbooth.imageprocessing.operations.producers.ImageProducingOperation.MAKE_PREVIEW;

public class PreviewPipeline implements Pipeline {


    private static final ImageProducingOperation PRODUCER = MAKE_PREVIEW;
    private static final List<ImageProcessingOperation> PROCESSORS = Collections.singletonList(MIRROR_IMAGE);
    private static final ImageConsumingOperation CONSUMER = SHOW_PREVIEW;


    private List<Executable> pipeline;


    public PreviewPipeline() throws OperationCreationException{

        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();

        ImageProcessingExecutable imageProcessingExecutable = applicationContext.getBean(ImageProcessingExecutable.class);
        imageProcessingExecutable.setOperations(PRODUCER, PROCESSORS, CONSUMER);

        pipeline = Collections.singletonList(imageProcessingExecutable);

    }

    @Override
    public void runWith(Executor executor) throws StopExecutionException {
        executor.execute(pipeline);
    }
}
