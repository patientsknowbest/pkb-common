package com.pkb.common.csv;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.Iterators.peekingIterator;
import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.StreamSupport.stream;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.PeekingIterator;

public class ApacheCsvParserWrapper implements CsvParser {

    private static final int FIRST_LINE = 1;

    private interface CloseAction {
        void execute();
    }

    private final PeekingIterator<CSVRecord> rows;
    private final boolean containsData;
    private final ImmutableList<String> headerNames;
    private final CloseAction closeAction;

    /**
     * WARNING: Building on top of an implementation detail: header map is a LinkedHashMap so its key set reflects real column order.
     */
    public ApacheCsvParserWrapper(CSVFormat delegate, Reader reader) {
        try {
            CSVParser parser = checkNotNull(delegate).withHeader().parse(reader);

            this.rows = peekingIterator(parser.iterator());
            this.containsData = rows.hasNext();
            this.headerNames = copyOf(parser.getHeaderMap().keySet());
            this.closeAction = () -> {
                try {
                    parser.close();
                } catch (Exception cause) {
                    // swallow it.
                }
            };
        } catch (IOException cause) {
            throw new IllegalStateException(cause);
        }
    }

    @NotNull @Override
    public Stream<CsvRecord> rows() {
        if (containsData) {
            checkState(rows.hasNext(), "Content of CSV was already used.");
        }

        AtomicLong rowCounter = new AtomicLong(FIRST_LINE);
        return stream(spliteratorUnknownSize(rows, Spliterator.ORDERED), false)
                .map(row -> new ImmutableCsvRecord(rowCounter.incrementAndGet(), row.getRecordNumber() + 1, createLookup(row)))
                .peek(csvRecord -> {
                    if (!rows.hasNext()) {
                        close();
                    }
                })
                .map(CsvRecord.class::cast);
    }

    @Override
    public boolean containsData() {
        return containsData;
    }

    @NotNull @Override
    public List<String> headerNames() {
        return headerNames;
    }

    @Override
    public void close() {
        closeAction.execute();
    }

    private Map<String, String> createLookup(CSVRecord row) {
        Map<String, String> lookup = new HashMap<>();
        headerNames.forEach(headerName -> lookup.put(headerName, EMPTY));
        lookup.putAll(row.toMap());
        return lookup;
    }
}
