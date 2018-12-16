package org.openbooth.imageprocessing.actions.impl;

import org.openbooth.config.key.ConfigIntegerKeys;
import org.openbooth.gui.ShotFrameController;
import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.actions.Action;
import org.openbooth.imageprocessing.actions.ActionFactory;
import org.openbooth.imageprocessing.exception.handler.impl.IgnoringExceptionHandler;
import org.openbooth.imageprocessing.execution.executor.Executor;
import org.openbooth.imageprocessing.execution.executor.impl.TimeLimitedExecutor;
import org.openbooth.imageprocessing.execution.pipelines.impl.PreviewPipeline;
import org.openbooth.service.InformationDistributor;
import org.openbooth.storage.KeyValueStore;
import org.openbooth.storage.exception.KeyValueStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ShowCountdownAcFac implements ActionFactory {
    private ShotFrameController shotFrameController;

    private KeyValueStore keyValueStore;
    private InformationDistributor informationDistributor;

    private PreviewPipeline previewPipeline;
    private IgnoringExceptionHandler ignoringExceptionHandler;

    public ShowCountdownAcFac(ShotFrameController shotFrameController, KeyValueStore keyValueStore, PreviewPipeline previewPipeline, InformationDistributor informationDistributor, IgnoringExceptionHandler ignoringExceptionHandler) {
        this.shotFrameController = shotFrameController;
        this.keyValueStore = keyValueStore;
        this.informationDistributor = informationDistributor;
        this.previewPipeline = previewPipeline;
        this.ignoringExceptionHandler = ignoringExceptionHandler;
    }

    @Override
    public Action getAction() throws ProcessingException {
        try {
            if(!informationDistributor.isTimedShotEnabled()) return new EmptyAction();


            int counter = keyValueStore.getInt(ConfigIntegerKeys.SHOT_COUNTDOWN.key);
            int executionsPerSecond = keyValueStore.getInt(ConfigIntegerKeys.MAX_PREVIEW_REFRESH.key);
            Executor executor = new TimeLimitedExecutor(ignoringExceptionHandler, executionsPerSecond);
            return new ShowCountdownAction(shotFrameController, previewPipeline, executor, counter);
        } catch (KeyValueStoreException e) {
            throw new ProcessingException(e);
        }
    }
}
