package com.pkb.common.util;

import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Function;

import static com.pkb.common.util.FrameFilter.filter;

public class ConsistencyChecker {
    @FunctionalInterface
    public interface Condition {
        boolean holds();
    }

    public static LoggerStage forJiraKey(@NotNull String key) {
        return new LoggerStage(key);
    }

    public static final class LoggerStage {
        private final String key;

        private LoggerStage(String key) {
            this.key = key;
        }

        public ConditionToThrowExceptionStage logUsing(@NotNull BiConsumer<String, Throwable> logger) {
            return new ConditionToThrowExceptionStage(key, logger);
        }
    }

    public static final class ConditionToThrowExceptionStage {
        private final String key;
        private final BiConsumer<String, Throwable> logger;

        private ConditionToThrowExceptionStage(String key, BiConsumer<String, Throwable> logger) {
            this.key = key;
            this.logger = logger;
        }

        public ConditionStage andThrowExceptionIf(@NotNull Condition conditionToThrowException) {
            return new ConditionStage(key, logger, conditionToThrowException, FinalStage.REVERSE_ORDER_TASK_EXECUTOR, IllegalStateException::new);
        }

        public ConditionStage andThrowExceptionIf(@NotNull Condition conditionToThrowException, @NotNull Function<String, ? extends RuntimeException> exceptionFactory) {
            return new ConditionStage(key, logger, conditionToThrowException, FinalStage.REVERSE_ORDER_TASK_EXECUTOR, exceptionFactory);
        }

        public ConditionStage orThrowExceptionIf(@NotNull Condition conditionToThrowException) {
            return new ConditionStage(key, logger, conditionToThrowException, FinalStage.NATURAL_ORDER_TASK_EXECUTOR, IllegalStateException::new);
        }

        public ConditionStage orThrowExceptionIf(@NotNull Condition conditionToThrowException, @NotNull Function<String, ? extends RuntimeException> exceptionFactory) {
            return new ConditionStage(key, logger, conditionToThrowException, FinalStage.NATURAL_ORDER_TASK_EXECUTOR, IllegalStateException::new);
        }
    }

    public static final class ConditionStage {
        private final String key;
        private final BiConsumer<String, Throwable> logger;
        private final Condition conditionToThrowException;
        private final FinalStage.TaskExecutor taskExecutor;
        private final Function<String, ? extends RuntimeException> exceptionFactory;

        private ConditionStage(
                String key,
                BiConsumer<String, Throwable> logger,
                Condition conditionToThrowException,
                FinalStage.TaskExecutor taskExecutor,
                Function<String, ? extends RuntimeException> exceptionFactory) {
            this.key = key;
            this.logger = logger;
            this.conditionToThrowException = conditionToThrowException;
            this.taskExecutor = taskExecutor;
            this.exceptionFactory = exceptionFactory;
        }

        public FinalStage when(@NotNull Condition condition) {
            return new FinalStage(key, logger, conditionToThrowException, taskExecutor, exceptionFactory, condition);
        }
    }

    public static final class FinalStage {
        private interface TaskExecutor {
            void execute(Runnable taskA, Runnable taskB);
        }

        public static final FinalStage.TaskExecutor NATURAL_ORDER_TASK_EXECUTOR = (taskA, taskB) -> {
            taskA.run();
            taskB.run();
        };
        public static final FinalStage.TaskExecutor REVERSE_ORDER_TASK_EXECUTOR = (taskA, taskB) -> {
            taskB.run();
            taskA.run();
        };

        private final String jiraKey;
        private final BiConsumer<String, Throwable> logger;
        private final Condition conditionToThrowException;
        private final TaskExecutor taskExecutor;
        private final Function<String, ? extends RuntimeException> exceptionFactory;
        private final Condition condition;

        private FinalStage(
                String jiraKey,
                BiConsumer<String, Throwable> logger,
                Condition conditionToThrowException,
                TaskExecutor taskExecutor,
                Function<String, ? extends RuntimeException> exceptionFactory,
                Condition condition) {
            this.jiraKey = jiraKey;
            this.logger = logger;
            this.conditionToThrowException = conditionToThrowException;
            this.taskExecutor = taskExecutor;
            this.exceptionFactory = exceptionFactory;
            this.condition = condition;
        }

        public void withMessage(@NotNull String message) {
            if (condition.holds()) {
                String messageWithKey = String.format("%s - %s", jiraKey, message);
                RuntimeException exception = exceptionFactory.apply(message);

                taskExecutor.execute(() -> {
                    if (conditionToThrowException.holds()) {
                        throw exception;
                    }
                }, () -> logger.accept(messageWithKey, filter(exception.fillInStackTrace())));
            }
        }
    }
}
