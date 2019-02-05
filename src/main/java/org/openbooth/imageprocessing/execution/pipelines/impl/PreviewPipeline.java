package org.openbooth.imageprocessing.execution.pipelines.impl;

import org.openbooth.imageprocessing.exception.StopExecutionException;
import org.openbooth.imageprocessing.execution.executables.Executable;
import org.openbooth.imageprocessing.execution.executor.Executor;
import org.openbooth.imageprocessing.execution.executables.impl.ImageProcessingExecutable;
import org.openbooth.imageprocessing.execution.pipelines.Pipeline;
import org.openbooth.imageprocessing.processors.ImageProcessorFactory;
import org.openbooth.imageprocessing.processors.mirroring.MirrorImageProcFac;
import org.openbooth.imageprocessing.producer.camera.MakePreviewProcFac;
import org.openbooth.imageprocessing.consumer.gui.ShowPreviewProcFac;
import org.openbooth.storage.exception.StorageException;

import java.util.Collections;
import java.util.List;

public class PreviewPipeline implements Pipeline {

    private List<Executable> pipeline;


    public PreviewPipeline() throws StorageException{

        ImageProcessingExecutable imageProcessingExecutable = createImageProcessing();

        pipeline = Collections.singletonList(imageProcessingExecutable);

    }

    @Override
    public void runWith(Executor executor) throws StopExecutionException {
        executor.execute(pipeline);
    }

    private ImageProcessingExecutable createImageProcessing() throws StorageException {

        List<ImageProcessorFactory> processorFactories = Collections.singletonList(new MirrorImageProcFac());


        return new ImageProcessingExecutable(new MakePreviewProcFac(), processorFactories, new ShowPreviewProcFac());
    }
}
