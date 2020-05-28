package com.pkb.common.csv;

import static com.google.common.collect.ImmutableSet.copyOf;
import static com.pkb.common.csv.ParsingErrorsBuilder.parsingErrorAt;

import java.io.Reader;
import java.util.List;
import java.util.stream.Stream;

import com.pkb.common.base.Error;

import io.vavr.control.Either;

public class CsvOpener {
    private static final String NO_DATA_CODE = "csv.parsing.there.is.no.data";
    private static final String NO_DATA = "This file contains a correct header but no data";
    private static final String FAILED_FOR_UNEXPECTED_REASON_CODE = "csv.parsing.failed.for.unexpected.reason";
    private static final String FAILED_FOR_UNEXPECTED_REASON = "Failed for unexpected reason";

    private final CsvParserFactory parserFactory;
    private final HeaderValidator headerValidator;

    public CsvOpener(CsvParserFactory parserFactory, HeaderValidator headerValidator) {
        this.parserFactory = parserFactory;
        this.headerValidator = headerValidator;
    }

    public Either<ParsingError<List<Error>>, Stream<CsvRecord>> getCsvRecords(Reader csvContent) {
        Either<ParsingError<List<Error>>, Stream<CsvRecord>> results;

        try {
            CsvParser csvParser = parserFactory.createParser(csvContent);

            List<Error> headerValidationErrors = headerValidator.validateHeader(copyOf(csvParser.headerNames()));
            if (headerValidationErrors.isEmpty()) {
                if (csvParser.containsData()) {
                    results = Either.right(csvParser.rows());
                } else {
                    results = Either.left(parsingErrorAt(2, NO_DATA_CODE, NO_DATA));
                }
            } else {
                results = Either.left(parsingErrorAt(1, headerValidationErrors));
            }
        } catch (Exception cause) {
            results = Either.left(parsingErrorAt(-1, FAILED_FOR_UNEXPECTED_REASON_CODE, FAILED_FOR_UNEXPECTED_REASON));
        }

        return results;
    }
}
