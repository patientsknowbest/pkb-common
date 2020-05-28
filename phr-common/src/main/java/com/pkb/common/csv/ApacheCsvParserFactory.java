package com.pkb.common.csv;

import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.jetbrains.annotations.NotNull;

public class ApacheCsvParserFactory implements CsvParserFactory {
    @Override public @NotNull CsvParser createParser(@NotNull Reader csvContent) {
        return new ApacheCsvParserWrapper(CSVFormat.DEFAULT, csvContent);
    }
}
