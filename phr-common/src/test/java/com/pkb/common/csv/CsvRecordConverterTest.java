package com.pkb.common.csv;

import static com.github.karsaig.approvalcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class CsvRecordConverterTest {
    private static final String FIRST_COLUMN = "Column 1";
    private static final String SECOND_COLUMN = "Column 2";
    private static final String THIRD_COLUMN = "Column 3";

    private ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldJoinColumnsEscapedIfNeededWhenCsvRecordCorrespondsToHeaders() {
        // GIVEN

        CsvRecordConverter underTest = new CsvRecordConverter(ImmutableList.of(FIRST_COLUMN, SECOND_COLUMN, THIRD_COLUMN));
        CsvRecord csvRecord = new ImmutableCsvRecord(3L, ImmutableMap.of(
                FIRST_COLUMN, "a,b",
                SECOND_COLUMN, "multi\nline",
                THIRD_COLUMN, "some \"quoted text\" in column"));

        // WHEN
        String actualCsvLine = underTest.convertRecord(csvRecord);

        // THEN
        assertThat(actualCsvLine, is(equalTo("\"a,b\",\"multi\nline\",\"some \"\"quoted text\"\" in column\"")));
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenCsvRecordDoesNotCorrespondToHeaders() {
        // GIVEN

        CsvRecordConverter underTest = new CsvRecordConverter(ImmutableList.of(FIRST_COLUMN, SECOND_COLUMN));
        CsvRecord csvRecord = new ImmutableCsvRecord(3L, ImmutableMap.of(FIRST_COLUMN, "a,b"));

        // WHEN
        String actualCsvLine = underTest.convertRecord(csvRecord);

        // THEN
        assertThat(actualCsvLine, is(equalTo("\"a,b\",")));

    }

    @Rule
    public ExpectedException getExpectedException() {
        return expectedException;
    }
}