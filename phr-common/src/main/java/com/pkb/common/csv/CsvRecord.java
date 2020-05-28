package com.pkb.common.csv;

import java.util.Map;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a Line of a CSV file.
 */
public interface CsvRecord {
    /**
     * Logical line number where indexing starts from one (this value is displayed in e.g.: in excel).
     *
     * @return Logical line number.
     */
    long logicalLineNumber();

    /**
     * If a file contains multi line values then effective and logical line numbers can be significantly different.
     * E.g.: line number displayed in Excel and effective line number in file can be different when values contain line breaks.
     *
     * @return Physical line number.
     */
    long effectiveLineNumber();

    /**
     * <pre>
     * Attempts to find a non empty value of a column defined in argument in given record (row).
     * This methods should return value only it contains any non whitespace character.
     * If you need real value of a cell, use {@link #getColumn(String)}.
     * </pre>
     *
     * @return Content of a non empty cell (a cell is empty if it contains empty string or only whitespaces). Never {@code null}.
     */
    @NotNull Optional<String> findColumn(@NotNull String columnName);

    /**
     * <b>Note</b>:
     * Prefer {@link #findColumn(String)} over this method, because it reflects the uncertainty of finding a value in its type as well and can never return {@code null}.
     *
     * @return a nullable value of a column defined in argument in given record (row).
     */
    @Nullable String getColumn(@NotNull String name);

    /**
     * @return count of columns in CSV row.
     */
    int columnCount();

    /**
     * @return Immutable map representation of a CSV line.
     */
    @NotNull Map<String, String> toMap();
}
