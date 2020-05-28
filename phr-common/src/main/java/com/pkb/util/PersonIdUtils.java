package com.pkb.util;

import static java.lang.invoke.MethodHandles.lookup;

import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pkb.common.util.FrameFilter;

public class PersonIdUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(lookup().lookupClass());

    /**
     * Parses the provided string-format person id to a long and invokes the callback.
     * <p>
     * If the id cannot be parsed, it is logged with a filtered stacktrace.
     */
    @Nullable
    public static <T> T withParsedPersonId(@NotNull String unsafePersonId, Function<Long, T> callback) {
        try {
            return callback.apply(Long.parseLong(unsafePersonId));
        } catch (NumberFormatException ex) {
            LOGGER.warn("Non-numeric person id {}", unsafePersonId, FrameFilter.filter(ex));
        }

        return null;
    }
}
