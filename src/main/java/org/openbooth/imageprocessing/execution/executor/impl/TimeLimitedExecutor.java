package org.openbooth.imageprocessing.execution.executor.impl;

import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.exception.StopExecutionException;
import org.openbooth.imageprocessing.exception.handler.ProcessingExceptionHandler;
import org.openbooth.imageprocessing.execution.executables.Executable;
import org.openbooth.imageprocessing.execution.executor.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class TimeLimitedExecutor implements Executor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeLimitedExecutor.class);

    private ProcessingExceptionHandler exceptionHandler;
    private LocalTime timeOfLastExecution;
    private Duration durationBetweenExecutions;

    public TimeLimitedExecutor(ProcessingExceptionHandler exceptionHandler, int executionsPerSecond) {
        this.exceptionHandler = exceptionHandler;

        timeOfLastExecution = LocalTime.now();
        durationBetweenExecutions = Duration.of(1, ChronoUnit.SECONDS).dividedBy(executionsPerSecond);
    }

    @Override
    public void execute(List<Executable> executables) throws StopExecutionException {

        Duration timeSinceLastExecution = Duration.between(timeOfLastExecution, LocalTime.now());
        if(timeSinceLastExecution.compareTo(durationBetweenExecutions) < 0){
            try {
                Thread.sleep(durationBetweenExecutions.minus(timeSinceLastExecution).toMillis());
            } catch (InterruptedException e) {
                LOGGER.error("Exception during waiting for next execution of pipeline",e);
                Thread.currentThread().interrupt();
            }
        }

        try {
            for (Executable executable : executables) {
                executable.execute();
            }
        } catch (ProcessingException e) {
            exceptionHandler.handleProcessingException(e);
        }

        timeOfLastExecution = LocalTime.now();


    }
}
