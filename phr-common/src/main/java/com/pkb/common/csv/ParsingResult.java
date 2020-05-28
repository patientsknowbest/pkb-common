package com.pkb.common.csv;

public class ParsingResult<T> {
    private final long rowNumber;
    private final T result;

    public static <T> ParsingResult<T> resultAt(long lineNumber, T result) {
        return new ParsingResult<>(lineNumber, result);
    }

    private ParsingResult(long rowNumber, T result) {
        this.rowNumber = rowNumber;
        this.result = result;
    }

    public long getRowNumber() {
        return rowNumber;
    }

    public T getResult() {
        return result;
    }
}
