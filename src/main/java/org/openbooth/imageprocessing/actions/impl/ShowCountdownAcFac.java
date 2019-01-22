package org.openbooth.imageprocessing.actions.impl;

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
import org.openbooth.storage.ConfigStore;
import org.openbooth.storage.exception.KeyValueStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ShowCountdownAcFac implements ActionFactory {
    private ShotFrameController shotFrameController;

    private ConfigStore configStore;
    private ContextInformation contextInformation;

    private PreviewPipeline previewPipeline;
    private IgnoringExceptionHandler ignoringExceptionHandler;

    public ShowCountdownAcFac(ShotFrameController shotFrameController, ConfigStore configStore, PreviewPipeline previewPipeline, ContextInformation contextInformation, IgnoringExceptionHandler ignoringExceptionHandler) {
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
        } catch (KeyValueStoreException e) {
            throw new ProcessingException(e);
        }
    }
}
