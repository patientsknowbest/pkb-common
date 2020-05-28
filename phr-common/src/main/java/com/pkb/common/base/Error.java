package com.pkb.common.base;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNoneBlank;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <pre>
 * Captures:
 *  - an error code (that can be used for e.g.: I18N),
 *  - a human readable error description and
 *  - bunch of context details that is needed for:
 *    a.) fill up human readable descriptions and
 *    b.) fill up I18N-ed text
 *    with details.
 * </pre>
 */
public class Error implements Serializable {

    public static String NOT_FOUND_ERROR_CODE = "not.found";
    public static String USER_TYPE_NOT_PERM_ERROR_CODE = "user.type.not.perm";

    public enum NHS_EMAIL_MISMATCH {
        EMPTY_FIELD,
        EMAIL_AND_DIFFERENT_NHS_NUMBER,
        NO_NHS_NUMBER_NO_EMAIL,
        MULTIPLE_EMAIL,
        MULTIPLE_NHS_NUMBER,
        MULTIPLE_EMAIL_AND_NHS_NUMBER,
        OUT_OF_AREA_PATIENT
    }

    private static final long serialVersionUID = 1L;

    private final String errorCode;
    private final String description;
    private final String[] arguments;


    public Error(@NotNull String errorCode, @NotNull String description, String... arguments) {
        checkArgument(isNoneBlank(errorCode), "Error code cannot be null nor empty.");
        checkArgument(isNoneBlank(description), "Error description cannot be null nor empty.");
        this.errorCode = errorCode;
        this.description = description;
        this.arguments = arguments;
    }

    public @NotNull String getErrorCode() {
        return errorCode;
    }

    public @Nullable String[] getArguments() {
        return arguments;
    }

    public @NotNull String getDescription() {
        return description;
    }

    public boolean hasArguments() {
        return arguments.length > 0;
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!canEqual(o)) {
            return false;
        }
        Error error = (Error) o;
        if (!errorCode.equals(error.errorCode)) {
            return false;
        }
        if (!description.equals(error.description)) {
            return false;
        }
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(arguments, error.arguments);
    }

    protected boolean canEqual(Object other) {
        return other instanceof Error;
    }

    @Override public int hashCode() {
        return Objects.hash(errorCode, description, arguments);
    }

    @Override public String toString() {
        return "Error [" + errorCode + "] (arguments=" + Arrays.toString(arguments) + ')';
    }
}
