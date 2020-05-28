package com.pkb.common.csv;

import static com.pkb.common.base.ErrorBuilder.error;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.pkb.common.base.Error;
import com.pkb.common.base.ErrorBuilder;

public class ParsingErrorsBuilder {
    private final long rowCounter;
    private final ImmutableList.Builder<Error> errorsCollector = ImmutableList.builder();

    private ErrorBuilder errorBuilder;

    public static ParsingError<List<Error>> parsingErrorAt(long rowCounter, String errorCode, String descriptionPatter, String... arguments) {
        return parsingError(rowCounter, errorCode, descriptionPatter).withArguments((Object[]) arguments).build();
    }

    public static ParsingError<List<Error>> parsingErrorAt(long rowCounter, List<Error> errors) {
        return new ParsingError<>(rowCounter, errors);
    }

    public static ParsingErrorsBuilder parsingError(long rowCounter, String errorCode, String descriptionPattern) {
        return new ParsingErrorsBuilder(rowCounter, errorCode, descriptionPattern);
    }

    private ParsingErrorsBuilder(long rowCounter, String errorCode, String descriptionPattern) {
        this.rowCounter = rowCounter;
        errorBuilder = error(errorCode, descriptionPattern);
    }

    public ParsingErrorsBuilder addError(String errorCode, String descriptionPattern) {
        errorsCollector.add(errorBuilder.build());
        errorBuilder = error(errorCode, descriptionPattern);
        return this;
    }

    public ParsingErrorsBuilder withArguments(Object... arguments) {
        errorBuilder.withArguments(arguments);
        return this;
    }

    @NotNull
    public ParsingError<List<Error>> build() {
        errorsCollector.add(errorBuilder.build());
        return new ParsingError<>(rowCounter, errorsCollector.build());
    }
}
