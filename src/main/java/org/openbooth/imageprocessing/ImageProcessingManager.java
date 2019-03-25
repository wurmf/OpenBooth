package org.openbooth.imageprocessing;

import org.openbooth.config.key.ConfigIntegerKeys;
import org.openbooth.imageprocessing.exception.OperationCreationException;
import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.exception.StopExecutionException;
import org.openbooth.imageprocessing.exception.handler.impl.StrictExceptionHandler;
import org.openbooth.imageprocessing.execution.executor.Executor;
import org.openbooth.imageprocessing.execution.executor.impl.StandardExecutor;
import org.openbooth.imageprocessing.execution.executor.impl.TimeLimitedExecutor;
import org.openbooth.imageprocessing.execution.pipelines.impl.PreviewPipeline;
import org.openbooth.imageprocessing.execution.pipelines.impl.ShotPipeline;
import org.openbooth.storage.ReadOnlyConfigStore;
import org.openbooth.storage.exception.ConfigStoreException;
import org.openbooth.trigger.TriggerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ImageProcessingManager extends Thread {

    private static final Logger LOGGER  = LoggerFactory.getLogger(ImageProcessingManager.class);

    private ReadOnlyConfigStore configStore;
    private TriggerManager triggerManager;

    private StrictExceptionHandler exceptionHandler;

    private boolean shouldStop = false;
    private boolean isProcessing = false;


    private boolean triggered = false;

    @Autowired
    public ImageProcessingManager(ReadOnlyConfigStore configStore, TriggerManager triggerManager, StrictExceptionHandler exceptionHandler) {
        this.configStore = configStore;
        this.triggerManager = triggerManager;
        this.exceptionHandler = exceptionHandler;
    }



    public synchronized void trigger() {
        triggered = true;
    }

    private void executePreviewPipeline(PreviewPipeline previewPipeline) throws StopExecutionException{


        try {
            int executionsPerSecond = configStore.getInt(ConfigIntegerKeys.MAX_PREVIEW_REFRESH.key);
            Executor executor = new TimeLimitedExecutor(exceptionHandler, executionsPerSecond);
            previewPipeline.runWith(executor);
        } catch (ConfigStoreException e) {
            exceptionHandler.handleProcessingException(new ProcessingException(e));
        }

    }

    public synchronized void stopProcessing(){
        shouldStop = true;
    }

    public boolean isProcessing(){return isProcessing;}

    @Override
    public void run() {
        try {
            PreviewPipeline previewPipeline = new PreviewPipeline();
            ShotPipeline shotPipeline = new ShotPipeline();

            isProcessing = true;

            triggerManager.setImageProcessingManager(this);

            Executor shotExecutor = new StandardExecutor(exceptionHandler);

            while(!shouldStop){
                if(triggered) {
                    shotPipeline.runWith(shotExecutor);
                    triggered = false;
                } else {
                    executePreviewPipeline(previewPipeline);
                }

            }

        } catch (StopExecutionException e) {
            LOGGER.error("Image processing terminated!");
        } catch ( OperationCreationException e){
            LOGGER.error("Could not create pipelines");
        } finally {
            LOGGER.info("Image processing stopped.");
            isProcessing = false;
        }
    }
}