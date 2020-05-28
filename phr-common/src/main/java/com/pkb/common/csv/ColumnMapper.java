package com.pkb.common.csv;

import org.jetbrains.annotations.NotNull;

import io.vavr.control.Either;

/**
 * Maps on or more columns into an object.
 *
 * @param <F> Type of Failure.
 * @param <S> Type of Success.
 */
@FunctionalInterface
public interface ColumnMapper<F, S> {
    @NotNull Either<F, S> map(@NotNull CsvRecord row);
}