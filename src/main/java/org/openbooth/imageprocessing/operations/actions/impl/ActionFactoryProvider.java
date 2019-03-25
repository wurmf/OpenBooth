package org.openbooth.imageprocessing.operations.actions.impl;

import org.openbooth.config.key.ConfigIntegerKeys;
import org.openbooth.context.ContextInformation;
import org.openbooth.context.ShotType;
import org.openbooth.gui.ShotFrameController;
import org.openbooth.imageprocessing.exception.OperationCreationException;
import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.exception.handler.impl.IgnoringExceptionHandler;
import org.openbooth.imageprocessing.execution.executor.Executor;
import org.openbooth.imageprocessing.execution.executor.impl.TimeLimitedExecutor;
import org.openbooth.imageprocessing.execution.pipelines.impl.PreviewPipeline;
import org.openbooth.imageprocessing.operations.OperationFactory;
import org.openbooth.imageprocessing.operations.OperationFactoryProvider;
import org.openbooth.imageprocessing.operations.actions.Action;
import org.openbooth.imageprocessing.operations.actions.ActionOperation;
import org.openbooth.storage.ReadOnlyConfigStore;
import org.openbooth.storage.exception.ConfigStoreException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ActionFactoryProvider implements OperationFactoryProvider<Action, ActionOperation> {

    private ContextInformation contextInformation;
    private ReadOnlyConfigStore configStore;
    private IgnoringExceptionHandler ignoringExceptionHandler;
    private ShotFrameController shotFrameController;
    private PreviewPipeline previewPipeline;


    @Autowired
    private ActionFactoryProvider(ApplicationContext applicationContext) throws OperationCreationException {
        this.shotFrameController = applicationContext.getBean(ShotFrameController.class);
        this.configStore = applicationContext.getBean(ReadOnlyConfigStore.class);
        this.contextInformation = applicationContext.getBean(ContextInformation.class);
        this.previewPipeline = new PreviewPipeline();
        this.ignoringExceptionHandler = applicationContext.getBean(IgnoringExceptionHandler.class);
    }

    @Override
    public OperationFactory<Action> getOperationFactory(ActionOperation operation) throws OperationCreationException {
        switch (operation) {
            case SHOW_COUNTDOWN:
                return createShowCountdownFactory();

            default:
                throw new OperationCreationException("There is not factory for ActionOperation " + operation);
        }
    }

    private OperationFactory<Action> createShowCountdownFactory(){
        return () -> {
            try {
                if(contextInformation.getShotType() != ShotType.TIMED) return new EmptyAction();

                int counter = configStore.getInt(ConfigIntegerKeys.SHOT_COUNTDOWN.key);
                int executionsPerSecond = configStore.getInt(ConfigIntegerKeys.MAX_PREVIEW_REFRESH.key);
                Executor executor = new TimeLimitedExecutor(ignoringExceptionHandler, executionsPerSecond);
                return new ShowCountdownAction(shotFrameController, previewPipeline, executor, counter);
            } catch (ConfigStoreException e) {
                throw new ProcessingException(e);
            }
        };
    }
}
