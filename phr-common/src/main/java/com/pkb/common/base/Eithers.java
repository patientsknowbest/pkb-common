package com.pkb.common.base;

import static java.util.Optional.empty;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import org.jetbrains.annotations.NotNull;

import io.vavr.control.Either;

/**
 * Utility methods for {@link Either}.
 */
public class Eithers {
    private static final Either<?, Optional<?>> EMPTY_SUCCESS = Either.right(empty());

    private Eithers() {
    }

    /**
     * <pre>
     * <code>
     *
     * Either&lt;F, ContactDetailDto&gt; validatedContactDetail = Eithers.combine(
     *     validatedName,
     *     validatedEmail,
     *     (name, email) -> ContactDetailDto.builder().name(name).email(email).build());
     * </code>
     * </pre>
     */
    public static <F, A, B, T> @NotNull Either<List<F>, T> combine(@NotNull Either<List<F>, A> a, @NotNull Either<List<F>, B> b, @NotNull BiFunction<A, B, T> constructor) {
        Either<List<F>, T> result;

        if (a.isRight() && b.isRight()) {
            return Either.right(constructor.apply(a.get(), b.get()));
        } else {
            return Either.left(io.vavr.collection.List
                    .<F>empty()
                    .appendAll(a.left().getOrElse(Collections.emptyList()))
                    .appendAll(b.left().getOrElse(Collections.emptyList()))
                    .toJavaList());
        }
    }
}
