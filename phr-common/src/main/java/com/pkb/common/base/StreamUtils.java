package com.pkb.common.base;

import static java.util.function.Function.identity;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;

public class StreamUtils {
    /**
     * <pre>
     * Similar to {@link java.util.stream.Stream#concat(Stream, Stream)} except that this can take more than two streams.
     * </pre>
     * <b>Warning</b>: Only one end-less stream can be passed in as argument on the last position. Otherwise, subsequent stream items are not getting used.
     * @return {@link Stream} of all items from streams given in argument.
     */
    @SafeVarargs
    public static <T> @NotNull Stream<T> concat(Stream<T> stream, Stream<T>... moreStreams) {
        return Stream.concat(stream, Stream.of(moreStreams).flatMap(identity()));
    }

    /**
     * Pull N elements based on an iterator, or less if the stream ends.
     * @param iterator The iterator
     * @param maxElements Max no. of elements to pull*
     * @return List of results
     */
    public static <T> @NotNull List<T> pull(@NotNull Iterator<? extends T> iterator, int maxElements) {
        List<T> ret = new LinkedList<>();
        while (ret.size()<maxElements && iterator.hasNext()) {
            ret.add(iterator.next());
        }
        return ret;
    }
}
