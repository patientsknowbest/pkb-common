package com.pkb.common.csv;

import static com.github.karsaig.approvalcrest.MatcherAssert.assertThat;
import static com.github.karsaig.approvalcrest.matcher.Matchers.sameBeanAs;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import org.apache.commons.csv.CSVFormat;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class ApacheCsvParserWrapperTest {
    private static final int SECOND_ROW = 2;
    private static final int THIRD_ROW = 3;

    private ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldReportNoDataWhenCsvContentIsEmpty() {
        // GIVEN

        CsvParser parser = new ApacheCsvParserWrapper(CSVFormat.DEFAULT, csvContent());

        // WHEN
        boolean actualValue = parser.containsData();

        // THEN
        assertThat(actualValue, is(equalTo(false)));
    }

    @Test
    public void shouldReportNoDataWhenCsvContentContainsOnlyHeader() {
        // GIVEN

        CsvParser parser = new ApacheCsvParserWrapper(CSVFormat.DEFAULT, csvContent("A,B"));

        // WHEN
        boolean actualValue = parser.containsData();

        // THEN
        assertThat(actualValue, is(equalTo(false)));
    }

    @Test
    public void shouldReportDataWhenCsvContentContainsHeaderAndAtLeastOneLine() {
        // GIVEN

        CsvParser parser = new ApacheCsvParserWrapper(CSVFormat.DEFAULT, csvContent("A,B", "a,b"));

        // WHEN
        boolean actualValue = parser.containsData();

        // THEN
        assertThat(actualValue, is(equalTo(true)));
    }

    @Test
    public void shouldReportNoHeadersWhenCsvContentIsEmpty() {
        // GIVEN

        CsvParser parser = new ApacheCsvParserWrapper(CSVFormat.DEFAULT, csvContent());

        // WHEN
        List<String> actualValue = parser.headerNames();

        // THEN
        assertThat(actualValue, emptyCollectionOf(String.class));
    }

    @Test
    public void shouldReportHeadersWhenCsvContentContainsHeader() {
        // GIVEN

        CsvParser parser = new ApacheCsvParserWrapper(CSVFormat.DEFAULT, csvContent("F,B,I"));

        // WHEN
        List<String> actualValue = parser.headerNames();

        // THEN
        assertThat(actualValue, sameBeanAs(ImmutableList.of("F", "B", "I")));
    }

    @Test
    public void shouldReturnEmptyStreamWhenCsvContentIsEmpty() {
        // GIVEN
        CsvParser parser = new ApacheCsvParserWrapper(CSVFormat.DEFAULT, csvContent());

        // WHEN
        Stream<CsvRecord> actualRows = parser.rows();

        // THEN
        assertThat(actualRows.collect(toList()), sameBeanAs(emptyList()));
    }

    @Test
    public void shouldStreamRows() {
        // GIVEN
        CsvParser parser = new ApacheCsvParserWrapper(CSVFormat.DEFAULT, csvContent("A,B", "a,b", "c,d"));

        // WHEN
        Stream<CsvRecord> actualRows = parser.rows();

        // THEN
        assertThat(actualRows.collect(toList()), sameBeanAs(ImmutableList.of(
                new ImmutableCsvRecord(SECOND_ROW, ImmutableMap.of("A", "a", "B", "b")),
                new ImmutableCsvRecord(THIRD_ROW, ImmutableMap.of("A", "c", "B", "d")))));
    }

    @Test
    public void shouldReturnAllRowsWhenCheckedForContainingDataAnyTimes() {
        // GIVEN
        CsvParser parser = new ApacheCsvParserWrapper(CSVFormat.DEFAULT, csvContent("A,B", "a,b", "c,d"));

        // WHEN
        boolean firstRead = parser.containsData();
        Stream<CsvRecord> actualRows = parser.rows();

        // THEN
        assertThat(firstRead, is(equalTo(true)));
        assertThat(actualRows.collect(toList()), sameBeanAs(ImmutableList.of(
                new ImmutableCsvRecord(2, ImmutableMap.of("A", "a", "B", "b")),
                new ImmutableCsvRecord(3, ImmutableMap.of("A", "c", "B", "d")))));
    }

    @Test
    public void shouldThrowIllegalStateExceptionWhenAttemptingToReadRowsForSecondsTime() {
        // GIVEN
        CsvParser parser = new ApacheCsvParserWrapper(CSVFormat.DEFAULT, csvContent("A,B", "a,b", "c,d"));
        assertThat(parser.rows().count(), is(equalTo(2L)));

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Content of CSV was already used.");

        // WHEN - THEN
        parser.rows();
    }

    @Test
    public void shouldReturnEmptyStringWhenValueIsEmpty() {
        // GIVEN
        CsvParser parser = new ApacheCsvParserWrapper(CSVFormat.DEFAULT, csvContent("A,B", "a,"));

        // WHEN
        Stream<CsvRecord> actualRows = parser.rows();

        // THEN
        assertThat(actualRows.collect(toList()), sameBeanAs(ImmutableList.of(
                new ImmutableCsvRecord(2, ImmutableMap.of("A", "a", "B", "")))));
    }

    @Test
    public void shouldKeepWhiteSpaces() {
        // GIVEN
        CsvParser parser = new ApacheCsvParserWrapper(CSVFormat.DEFAULT, csvContent("A,B,C", "a,    ,"));

        // WHEN
        Stream<CsvRecord> actualRows = parser.rows();

        // THEN
        assertThat(actualRows.collect(toList()), sameBeanAs(ImmutableList.of(
                new ImmutableCsvRecord(2, ImmutableMap.of("A", "a", "B", "    ", "C", "")))));
    }

    @Test
    public void shouldFillInColumnsWithEmptyStringsWhenTheyAreAbsent() {
        // GIVEN
        CsvParser parser = new ApacheCsvParserWrapper(CSVFormat.DEFAULT, csvContent("A,B,C", "a,"));

        // WHEN
        Stream<CsvRecord> actualRows = parser.rows();

        // THEN
        assertThat(actualRows.collect(toList()), sameBeanAs(ImmutableList.of(
                new ImmutableCsvRecord(2, ImmutableMap.of("A", "a", "B", "", "C", "")))));
    }

    @Test
    public void shouldThrowIllegalStateExceptionWhenInstantiationFailsDueToIOException() {
        // GIVEN

        thrown.expect(IllegalStateException.class);

        // WHEN - THEN
        new ApacheCsvParserWrapper(CSVFormat.DEFAULT, new ExceptionThrowingReader());
    }

    @Test
    public void shouldInvokeCloseWhenReachedEndOfStream() {
        // GIVEN

        CloseCountingReader reader = new CloseCountingReader(Joiner.on('\n').join("A", "a", "b"));
        CsvParser parser = new ApacheCsvParserWrapper(CSVFormat.DEFAULT, reader);

        // GIVEN
        long actualRowCount = parser.rows().count();

        // THEN
        assertThat(actualRowCount, is(equalTo(2L)));
        assertThat(reader.closeMethodInvocationCount(), is(equalTo(1L)));
    }

    @Test
    public void shouldSurviveWhenClosingFails() {
        // GIVEN

        CsvParser parser = new ApacheCsvParserWrapper(CSVFormat.DEFAULT, new ExceptionThrowingOnCloseReader(Joiner.on('\n').join("A", "a", "b")));

        // GIVEN
        long actualRowCount = parser.rows().count();

        // THEN
        assertThat(actualRowCount, is(equalTo(2L)));
    }

    @Rule
    public ExpectedException getThrown() {
        return thrown;
    }

    // ========== Helper methods below. ==========

    private Reader csvContent(String... lines) {
        return new StringReader(Joiner.on('\n').join(lines));
    }

    private static final class ExceptionThrowingReader extends Reader {

        @Override
        public int read(char[] cbuf, int off, int len) throws IOException {
            throw new IOException();
        }

        @Override
        public void close() throws IOException {

        }
    }

    private static final class CloseCountingReader extends StringReader {
        private final AtomicLong invocationCount = new AtomicLong();

        CloseCountingReader(String content) {
            super(content);
        }

        @Override
        public void close() {
            invocationCount.incrementAndGet();
            super.close();
        }

        long closeMethodInvocationCount() {
            return invocationCount.get();
        }
    }

    private static final class ExceptionThrowingOnCloseReader extends StringReader {
        ExceptionThrowingOnCloseReader(String content) {
            super(content);
        }

        @Override
        public void close() {
            throw new IllegalStateException();
        }
    }

}