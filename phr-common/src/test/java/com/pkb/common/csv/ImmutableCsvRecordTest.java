package com.pkb.common.csv;

import static com.github.karsaig.approvalcrest.MatcherAssert.assertThat;
import static com.github.karsaig.approvalcrest.matcher.Matchers.sameBeanAs;
import static java.util.Optional.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsEqual.equalTo;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.collect.ImmutableMap;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class ImmutableCsvRecordTest {
    @Test
    public void shouldFindEmptyOptionalWhenGivenColumnIsMissing() {
        // GIVEN
        CsvRecord underTest = new ImmutableCsvRecord(3, ImmutableMap.of());

        // WHEN
        Optional<String> actualValue = underTest.findColumn("non existing column");

        // THEN
        assertThat(actualValue, sameBeanAs(empty()));
    }

    @Test
    public void shouldFindValueWhenColumnPresent() {
        // GIVEN
        CsvRecord underTest = new ImmutableCsvRecord(3, ImmutableMap.of("Column 1", "a"));

        // WHEN
        Optional<String> actualValue = underTest.findColumn("Column 1");

        // THEN
        assertThat(actualValue, sameBeanAs(Optional.of("a")));
    }

    @Test
    public void shouldReturnColumnCount() {
        // GIVEN
        CsvRecord underTest = new ImmutableCsvRecord(3, ImmutableMap.of("Column 1", "a"));

        // WHEN
        int actualColumnCount = underTest.columnCount();

        // THEN
        assertThat(actualColumnCount, is(equalTo(1)));
    }

    @Test
    public void shouldReturnLineNumber() {
        // GIVEN
        CsvRecord underTest = new ImmutableCsvRecord(3, ImmutableMap.of());

        // WHEN
        long actualLineNumber = underTest.logicalLineNumber();

        // THEN
        assertThat(actualLineNumber, is(equalTo(3L)));
    }

    @Test
    public void shouldReturnNullWhenColumnValueIsAbsent() {
        // GIVEN
        CsvRecord underTest = new ImmutableCsvRecord(3, ImmutableMap.of());

        // WHEN
        String actualValue = underTest.getColumn("non existing column");

        // THEN
        assertThat(actualValue, nullValue());
    }

    @Test
    public void shouldReturnValueWhenColumnValueIsPresent() {
        // GIVEN
        CsvRecord underTest = new ImmutableCsvRecord(3, ImmutableMap.of("Column 1", "abc"));

        // WHEN
        String actualValue = underTest.getColumn("Column 1");

        // THEN
        assertThat(actualValue, is(equalTo("abc")));
    }

}