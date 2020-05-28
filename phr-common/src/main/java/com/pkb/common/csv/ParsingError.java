package com.pkb.common.csv;

import org.jetbrains.annotations.NotNull;

/**
 * Captures one or more error in parsing one single CSV line.
 */
public class ParsingError<E> {
    private final long lineNumber;
    private final E error;

    public ParsingError(long lineNumber, @NotNull E error) {
        this.lineNumber = lineNumber;
        this.error = error;
    }

    public long getLineNumber() {
        return lineNumber;
    }

    public @NotNull E getError() {
        return error;
    }
}
