package org.openbooth.imageprocessing.actions.impl;

import org.openbooth.application.ApplicationContextProvider;
import org.openbooth.config.key.ConfigIntegerKeys;
import org.openbooth.context.ShotType;
import org.openbooth.gui.ShotFrameController;
import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.actions.Action;
import org.openbooth.imageprocessing.actions.ActionFactory;
import org.openbooth.imageprocessing.exception.handler.impl.IgnoringExceptionHandler;
import org.openbooth.imageprocessing.execution.executor.Executor;
import org.openbooth.imageprocessing.execution.executor.impl.TimeLimitedExecutor;
import org.openbooth.imageprocessing.execution.pipelines.impl.PreviewPipeline;
import org.openbooth.context.ContextInformation;
import org.openbooth.storage.ReadOnlyConfigStore;
import org.openbooth.storage.exception.ConfigStoreException;
import org.openbooth.storage.exception.StorageException;
import org.springframework.context.ApplicationContext;


public class ShowCountdownAcFac implements ActionFactory {
    private ShotFrameController shotFrameController;

    private ReadOnlyConfigStore configStore;
    private ContextInformation contextInformation;

    private PreviewPipeline previewPipeline;
    private IgnoringExceptionHandler ignoringExceptionHandler;

    public ShowCountdownAcFac() throws StorageException {
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        this.shotFrameController = applicationContext.getBean(ShotFrameController.class);
        this.configStore = applicationContext.getBean(ReadOnlyConfigStore.class);
        this.contextInformation = applicationContext.getBean(ContextInformation.class);
        this.previewPipeline = new PreviewPipeline();
        this.ignoringExceptionHandler = applicationContext.getBean(IgnoringExceptionHandler.class);
    }

    public ShowCountdownAcFac(ShotFrameController shotFrameController, ReadOnlyConfigStore configStore, PreviewPipeline previewPipeline, ContextInformation contextInformation, IgnoringExceptionHandler ignoringExceptionHandler) {
        this.shotFrameController = shotFrameController;
        this.configStore = configStore;
        this.contextInformation = contextInformation;
        this.previewPipeline = previewPipeline;
        this.ignoringExceptionHandler = ignoringExceptionHandler;
    }

    @Override
    public Action getAction() throws ProcessingException {
        try {
            if(contextInformation.getShotType() != ShotType.TIMED) return new EmptyAction();


            int counter = configStore.getInt(ConfigIntegerKeys.SHOT_COUNTDOWN.key);
            int executionsPerSecond = configStore.getInt(ConfigIntegerKeys.MAX_PREVIEW_REFRESH.key);
            Executor executor = new TimeLimitedExecutor(ignoringExceptionHandler, executionsPerSecond);
            return new ShowCountdownAction(shotFrameController, previewPipeline, executor, counter);
        } catch (ConfigStoreException e) {
            throw new ProcessingException(e);
        }
    }
}
