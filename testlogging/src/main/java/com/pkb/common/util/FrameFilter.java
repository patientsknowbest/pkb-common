package com.pkb.common.util;

import java.util.Arrays;
import java.util.regex.Pattern;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import io.vavr.collection.List;

@SuppressWarnings({"unused", "WeakerAccess"})
public class FrameFilter {

    private static Pattern PKB_CODE = Pattern.compile("^com\\.pkb\\..*$");
    private static Pattern EXCLUDE_PKB_INTERCEPTORS = Pattern.compile("^(?!com.pkb.interceptor).*$");
    private static Pattern EXCLUDE_PKB_SERVLETS = Pattern.compile("^(?!com.pkb.servlets).*$");

    private static List<Pattern> DEFAULT_PATTERNS = List.of(PKB_CODE, EXCLUDE_PKB_INTERCEPTORS, EXCLUDE_PKB_SERVLETS);

    /**
     * Removes the stack frames that don't match the provided pattern
     *
     * @param frames           the frames
     * @param classNamePattern the pattern
     * @return a new array of the same frames
     */
    @NotNull
    @Contract(pure = true)
    public static StackTraceElement[] filter(StackTraceElement[] frames, Pattern classNamePattern) {
        return (StackTraceElement[]) Arrays.stream(frames.clone()).filter(f -> classNamePattern.matcher(f.getClassName()).matches()).toArray();
    }

    /**
     * Removes the stack frames from the exception that don't start with com.pkb
     *
     * @param e the exception
     * @return the provided exception with the non-matching frames removed
     */
    @NotNull
    @Contract("_ -> param1")
    public static Throwable filter(Throwable e) {
        return DEFAULT_PATTERNS.foldLeft(e, FrameFilter::filter);
    }


    /**
     * Removes the stack frames from the exception that don't match the provided pattern
     *
     * @param e the exception
     * @return the provided exception with the non-matching frames removed
     */
    @NotNull
    @Contract("_,_ -> param1")
    public static Throwable filter(Throwable e, Pattern classNamePattern) {
        List<StackTraceElement> elements = List.of(e.getStackTrace());
        StackTraceElement[] filteredFrames = elements.take(1).appendAll(elements.tail().filter(f -> classNamePattern.matcher(f.getClassName()).matches()).map(StackTraceElement.class::cast)).toJavaArray(size -> new StackTraceElement[size]);
        e.setStackTrace(filteredFrames);
        return e;
    }
}
