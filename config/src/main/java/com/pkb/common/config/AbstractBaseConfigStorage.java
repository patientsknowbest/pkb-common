package com.pkb.common.config;

import io.vavr.control.Either;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.Function;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;
import static java.util.function.Function.identity;

abstract class AbstractBaseConfigStorage implements ConfigStorage {

    protected static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

    protected interface Parser<P> {
        Optional<P> parse(String rawValue);
    }

    private <P> Either<ConfigurationException, P> readValue(String key, Class<P> expectedType, Parser<P> parser) {
        return parseValue(key, getString(key), expectedType, parser);
    }

    private Optional<Boolean> parseBooleanLiteral(String str) {
        String normalizedRawString = str == null ? "" : str.trim().toLowerCase();
        if ("true".equals(normalizedRawString)) {
            return Optional.of(true);
        } else if ("false".equals(normalizedRawString)) {
            return Optional.of(false);
        }
        return Optional.empty();
    }

    private <N extends Number> AbstractBaseConfigStorage.Parser<N> createNumberParser(Function<String, N> wrappedParser) {
        return str -> {
            try {
                return Optional.of(wrappedParser.apply(str));
            } catch (NumberFormatException e) {
                LOGGER.error("Bad config property value [{}] was supposed to be  a number but wasn't parseable", str, e);
                return Optional.empty();
            }
        };
    }

    @Override
    public Boolean getBoolean(String key) {
        return readValue(key, Boolean.class, this::parseBooleanLiteral).getOrElseThrow(identity());
    }

    @Override
    public Boolean getBoolean(String key, Boolean defaultValue) {
        return readValue(key, Boolean.class, this::parseBooleanLiteral).getOrElseGet(exc -> Match(exc).of(
                Case($(instanceOf(MissingValueException.class)), defaultValue),
                Case($(instanceOf(MalformedValueException.class)), e -> {
                    throw e;
                })
        ));
    }

    @Override
    public Integer getInt(String key) {
        return readValue(key, Integer.class, createNumberParser(Integer::parseInt)).getOrElseThrow(identity());
    }

    @Override
    public Integer getInt(String key, int defaultValue) {
        return readValue(key, Integer.class, createNumberParser(Integer::parseInt)).getOrElse(defaultValue);
    }

    @Override
    public Long getLong(String key) {
        return readValue(key, Long.class, createNumberParser(Long::parseLong)).getOrElseThrow(identity());
    }

    @Override
    public Long getLong(String key, long defaultValue) {
        return readValue(key, Long.class, createNumberParser(Long::parseLong)).getOrElse(defaultValue);
    }

    private <P> Either<ConfigurationException, P> parseValue(String key, String rawValue, Class<P> expectedType, Parser<P> parser) {
        return parser.parse(rawValue)
                .map((Function<P, Either<ConfigurationException, P>>) Either::right)
                .orElseGet(() -> {
                    ConfigurationException exception;
                    if (rawValue == null) {
                        exception = new MissingValueException(key);
                    } else {
                        exception = new MalformedValueException(key, expectedType, rawValue);
                    }
                    return Either.left(exception);
                });
    }
}
