package com.pkb.common.csv;

import java.io.Reader;

import org.jetbrains.annotations.NotNull;

public interface CsvParserFactory {
    @NotNull CsvParser createParser(@NotNull Reader csvContent);
}
