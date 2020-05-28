package com.pkb.common.csv;

import java.util.List;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface RowValidator<E> {
    @NotNull
    List<E> validate(@NotNull CsvRecord row);
}
