package com.pkb.common.config;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;
import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.util.function.Function.identity;

import io.vavr.control.Either;

public class RawConfigStorage {

    private interface Parser<P> {
        Optional<P> parse(String rawValue);
    }

    public static final RawConfigStorage EMPTY = new RawConfigStorage(emptyMap());

    public static RawConfigStorage createDefault() {
        return LayeredLoader.defaultLoader().load();
    }

    public static RawConfigStorage merge(RawConfigStorage original, RawConfigStorage overrides) {
        Map<String, String> mergedStorage = new HashMap<>(original.storage);
        mergedStorage.putAll(overrides.storage);
        return new RawConfigStorage(unmodifiableMap(mergedStorage));
    }

    private final Map<String, String> storage;

    public RawConfigStorage(Map<String, String> storage) {
        this.storage = storage;
    }

    private <P> Either<ConfigurationException, P> readValue(String key, Parser<P> parser) {
        String rawValue = storage.get(key);
        return parser.parse(rawValue)
                // noinspection
                .map(p -> Either.<ConfigurationException, P>right(p))
                .orElseGet(() -> {
                    ConfigurationException exception;
                    if (rawValue == null) {
                        exception = new MissingValueException(key);
                    } else {
                        exception = new MalformedValueException(key, Boolean.class, rawValue);
                    }
                    return Either.left(exception);
                });
    }

    private Optional<Boolean> parseBooleanLiteral(String str) {
        if ("true".equalsIgnoreCase(str)) {
            return Optional.of(true);
        } else if ("false".equalsIgnoreCase(str)) {
            return Optional.of(false);
        }
        return Optional.empty();
    }

    private <N extends Number> Parser<N> createNumberParser(Function<String, N> wrappedParser) {
        return str -> {
            try {
                return Optional.of(wrappedParser.apply(str));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        };
    }


    public Boolean getBoolean(String key) {
        return readValue(key, this::parseBooleanLiteral).getOrElseThrow(identity());
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        return readValue(key, this::parseBooleanLiteral).getOrElseGet(exc -> Match(exc).of(
                Case($(instanceOf(MissingValueException.class)), defaultValue),
                Case($(instanceOf(MalformedValueException.class)), e -> {throw e;})
        ));
    }

    public String getString(String key) {
        return storage.get(key);
    }

    public String getString(String key, String defaultValue) {
        return storage.getOrDefault(key, defaultValue);
    }

    public int getInt(String key) {
        return readValue(key, createNumberParser(Integer::parseInt)).getOrElseThrow(identity());
    }

    public int getInt(String key, int defaultValue) {
        return readValue(key, createNumberParser(Integer::parseInt)).getOrElse(defaultValue);
    }

    public long getLong(String key) {
        return readValue(key, createNumberParser(Long::parseLong)).getOrElseThrow(identity());
    }

    public long getLong(String key, long defaultValue) {
        return readValue(key, createNumberParser(Long::parseLong)).getOrElse(defaultValue);
    }

    public boolean containsKey(String key) {
     return storage.containsKey(key);
    }
}
