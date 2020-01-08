package com.pkb.common.util;

import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.pkb.common.util.FrameFilter.filter;
import static java.util.stream.Collectors.joining;

public class ConsistencyChecker {
    @FunctionalInterface
    public interface Condition {
        boolean holds();
    }

    public static LoggerStage forJiraKeys(@NotNull String key, @NotNull String... otherKeys) {
        return new LoggerStage(Stream.concat(Stream.of(key), Stream.of(otherKeys)).collect(joining()));
    }

    public static LoggerStage forJiraKey(@NotNull String key) {
        return new LoggerStage(key);
    }

    public static final class LoggerStage {
        private static final BiConsumer<String, Throwable> NOOP = ($1, $2) -> {
        };

        private final String key;

        private LoggerStage(String key) {
            this.key = key;
        }

        public ConditionToThrowExceptionStage withoutLogging() {
            return new ConditionToThrowExceptionStage(key, NOOP);
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

        public FinalStage whenNot(@NotNull Condition condition) {
            return new FinalStage(key, logger, conditionToThrowException, taskExecutor, exceptionFactory, () -> !condition.holds());
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

        private Runnable postConditionCheckTask;

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
            this.postConditionCheckTask = () -> {};
        }

        public FinalStage postCheck(Runnable postCheckTask) {
            this.postConditionCheckTask = postCheckTask;
            return this;
        }

        public void withMessage(@NotNull String message) {
            if (condition.holds()) {
                postConditionCheckTask.run();
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
