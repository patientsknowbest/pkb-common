package com.pkb.common.csv;

import java.io.Closeable;
import java.util.List;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;

public interface CsvParser extends AutoCloseable, Closeable {
    @NotNull
    Stream<CsvRecord> rows();

    boolean containsData();

    @NotNull
    List<String> headerNames();
}
