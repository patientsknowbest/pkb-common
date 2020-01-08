package com.pkb.common.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.function.BiConsumer;

import static com.pkb.common.util.ConsistencyChecker.forJiraKey;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class ConsistencyCheckerTest {
    public static final ConsistencyChecker.Condition TAUTOLOGY = () -> true;
    public static final ConsistencyChecker.Condition CONTRADICTION = () -> false;

    private static final ConsistencyChecker.Condition EXCEPTION_THROWING_ENABLED = TAUTOLOGY;
    private static final ConsistencyChecker.Condition EXCEPTION_THROWING_DISABLED = CONTRADICTION;

    private static final ConsistencyChecker.Condition CONDITION_HOLDS = TAUTOLOGY;
    private static final ConsistencyChecker.Condition CONDITION_DOESNT_HOLD = CONTRADICTION;

    @Mock
    private BiConsumer<String, Throwable> logger;

    @Test
    public void shouldBothLogAndThrowException() {
        try {
            // GIVEN
            willDoNothing().given(logger).accept(eq("PHR-5705 - Both log and throw exception"), any(IllegalStateException.class));
            // WHEN
            forJiraKey("PHR-5705")
                    .logUsing(logger)
                    .andThrowExceptionIf(EXCEPTION_THROWING_ENABLED)
                    .when(CONDITION_HOLDS)
                    .withMessage("Both log and throw exception");
        } catch (IllegalStateException expected) {
            verify(logger, times(1)).accept("PHR-5705 - Both log and throw exception", expected);
            // FIXME: remove stack traces of com.pkb.common.util.ConsistencyChecker
        }
    }

    @Test
    public void shouldDoNothingWhenBothExceptionThrowingAndLoggingIsConfiguredButConditionDoesNotHold() {
        // GIVEN - WHEN
        forJiraKey("PHR-5705")
                .logUsing(logger)
                .andThrowExceptionIf(EXCEPTION_THROWING_ENABLED)
                .when(CONDITION_DOESNT_HOLD)
                .withMessage("Nothing should happen");

        // THEN
        verifyZeroInteractions(logger);
    }

    @Test
    public void shouldOnlyThrowException() {
        // GIVEN
        try {
            forJiraKey("PHR-5705")
                    .logUsing(logger)
                    .orThrowExceptionIf(EXCEPTION_THROWING_ENABLED)
                    .when(CONDITION_HOLDS)
                    .withMessage("Both log and throw exception");
        } catch (IllegalStateException expected) {
            verifyZeroInteractions(logger);
        }
    }

    @Test
    public void shouldNotingHappenWhenExceptionThrowingIsConfiguredButConditionDoesNotHold() {
        // GIVEN - WHEN
        forJiraKey("PHR-5705")
                .logUsing(logger)
                .orThrowExceptionIf(EXCEPTION_THROWING_ENABLED)
                .when(CONDITION_DOESNT_HOLD)
                .withMessage("Nothing should happen");

        // THEN
        verifyZeroInteractions(logger);
    }

    @Test
    public void shouldOnlyLogWhenConsistencyCheckFailsAndExceptionIsDisabled() {
        // GIVEN
        willDoNothing().given(logger).accept(eq("PHR-5705 - Only Log when inconsistency found and exception throwing is disabled"), any(IllegalStateException.class));

        // WHEN
        forJiraKey("PHR-5705")
                .logUsing(logger)
                .andThrowExceptionIf(EXCEPTION_THROWING_DISABLED)
                .when(CONDITION_HOLDS)
                .withMessage("Only Log when inconsistency found and exception throwing is disabled");

        // THEN
        verify(logger, times(1)).accept(eq("PHR-5705 - Only Log when inconsistency found and exception throwing is disabled"), any(IllegalStateException.class));
    }

    @Test
    public void doNothingWhenOnlyExceptionThrowingIsConfiguredButConditionDoesntHold() {
        // GIVEN - WHEN
        forJiraKey("PHR-5705")
                .logUsing(logger)
                .andThrowExceptionIf(EXCEPTION_THROWING_DISABLED)
                .when(CONDITION_DOESNT_HOLD)
                .withMessage("Nothing should happen");

        // THEN
        verifyZeroInteractions(logger);
    }

    @Test
    public void shouldOnlyLog() {
        // GIVEN
        willDoNothing().given(logger).accept(eq("PHR-5705 - Should only log"), any(IllegalStateException.class));

        // WHEN
        forJiraKey("PHR-5705")
                .logUsing(logger)
                .orThrowExceptionIf(EXCEPTION_THROWING_DISABLED)
                .when(CONDITION_HOLDS)
                .withMessage("Should only log");

        // THEN
        verify(logger, times(1)).accept(eq("PHR-5705 - Should only log"), any(IllegalStateException.class));
    }

    @Test
    public void shouldNothingHappen() {
        // GIVEN - WHEN
        forJiraKey("PHR-5705")
                .logUsing(logger)
                .orThrowExceptionIf(EXCEPTION_THROWING_DISABLED)
                .when(CONDITION_DOESNT_HOLD)
                .withMessage("Nothing should happen");

        // THEN
        verifyZeroInteractions(logger);
    }
}
