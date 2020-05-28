package com.pkb.common.csv;

import static java.util.stream.Collectors.joining;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;

public class CsvRecordConverter {
    private final List<String> headers;
    private final String separator;

    public CsvRecordConverter(@NotNull List<String> headers) {
        this(",", headers);
    }

    public CsvRecordConverter(String separator, @NotNull List<String> headers) {
        this.separator = separator;
        this.headers = ImmutableList.copyOf(headers);
    }

    public @NotNull String convertRecord(@NotNull CsvRecord record) {
        return headers.stream()
                .map(record::getColumn)
                .map(StringUtils::trimToEmpty)
                .map(StringEscapeUtils::escapeCsv)
                .collect(joining(separator));
    }

    public @NotNull String getHeaderRow() {
        return String.join(separator, headers);
    }
}
