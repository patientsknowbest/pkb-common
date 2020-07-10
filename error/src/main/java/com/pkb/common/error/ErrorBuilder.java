package com.pkb.common.error;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNoneBlank;

import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;

public class ErrorBuilder {

    private static final Object[] EMPTY_ARGS = new Object[] {};

    private String errorCode;
    private String description;
    private Object[] arguments = EMPTY_ARGS;

    public static Error errorOf(String errorCode, String description, Object... arguments) {
        return error(errorCode, description).withArguments(arguments).build();
    }

    public static ErrorBuilder error(String errorCode, String description) {
        return new ErrorBuilder(errorCode, description);
    }

    private ErrorBuilder(@NotNull String errorCode, @NotNull String description) {
        checkArgument(isNoneBlank(errorCode), "Error code cannot be null nor empty.");
        checkArgument(isNoneBlank(description), "Error description cannot be null nor empty.");
        this.errorCode = errorCode;
        this.description = description;
    }

    public ErrorBuilder withArguments(Object... arguments) {
        this.arguments = arguments;
        return this;
    }

    public @NotNull Error build() {
        return new Error(errorCode, format(description, this.arguments), argumentsAsStrings());
    }

    private String[] argumentsAsStrings() {
        return Stream.of(arguments).map(Object::toString).toArray(String[]::new);
    }
}
