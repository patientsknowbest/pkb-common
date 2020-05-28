package com.pkb.common.csv;

import org.jetbrains.annotations.NotNull;

import io.vavr.control.Either;

@FunctionalInterface
public interface RowMapper<F, S> {
    @NotNull
    Either<F, S> map(@NotNull CsvRecord row);
}
