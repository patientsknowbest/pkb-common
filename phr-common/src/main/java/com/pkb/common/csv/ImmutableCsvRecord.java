package com.pkb.common.csv;

import static java.util.Optional.ofNullable;

import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableMap;

public class ImmutableCsvRecord implements CsvRecord {
    private final long logicalLineNumber;
    private final long effectiveLineNumber;
    private final ImmutableMap<String, String> lookup;

    public ImmutableCsvRecord(long logicalLineNumber, Map<String, String> lookup) {
        this(logicalLineNumber, logicalLineNumber, lookup);
    }

    public ImmutableCsvRecord(long logicalLineNumber, long effectiveLineNumber, Map<String, String> lookup) {
        this.logicalLineNumber = logicalLineNumber;
        this.effectiveLineNumber = effectiveLineNumber;
        this.lookup = ImmutableMap.copyOf(lookup);
    }

    @Override
    public long logicalLineNumber() {
        return logicalLineNumber;
    }

    @Override
    public long effectiveLineNumber() {
        return effectiveLineNumber;
    }

    @Override
    public @NotNull Optional<String> findColumn(@NotNull String columnName) {
        return ofNullable(getColumn(columnName))
                .map(StringUtils::trimToEmpty)
                .filter(StringUtils::isNoneBlank);
    }

    @Override
    public @Nullable String getColumn(@NotNull String columnName) {
        return lookup.get(columnName);
    }

    @Override
    public int columnCount() {
        return lookup.size();
    }

    @Override
    public @NotNull Map<String, String> toMap() {
        return lookup;
    }
}
