package com.pkb.common.base;

import static com.google.common.base.Preconditions.checkNotNull;

import org.jetbrains.annotations.NotNull;

import io.vavr.collection.List;

/**
 * <pre>
 *     Syntactic sugar to be used instead of expressions like:
 *     <code>
 *
 * List&lt;Either&lt;List&lt;Error&gt;, String&gt;&gt; validationResults = processRows(rows);
 *
 * ProcessingResults&lt;List&lt;Error&gt;, String&gt; validationResults = processRows(rows);
 *
 *     </code>
 *
 *     While it might yield same line length this form has the following two benefits:
 *      - name of types make is more explicit what a given variable holds,
 *      - its API is cleaner. e.g.: to fetch all failures and success are straight forward (otherwise filtering and casting would be needed).
 * </pre>
 *
 * @param <F> Failure type
 * @param <S> Success type
 */
public final class ProcessingResults<F, S> {
    private final List<F> failures;
    private final List<S> successes;

    public ProcessingResults(@NotNull List<F> failures, @NotNull List<S> successes) {
        this.successes = checkNotNull(successes, "Successful processing results cannot be empty.");
        this.failures = checkNotNull(failures, "Failure processing results cannot be empty.");
    }

    public List<F> getFailures() {
        return failures;
    }

    public List<S> getSuccesses() {
        return successes;
    }

}