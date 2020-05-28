package com.pkb.util;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;

public class Optionals {
    // https://stackoverflow.com/a/49184522
    // Can get rid of this once we're off java 8
    public static <T> Optional<T> or(Supplier<Optional<T>>... optionals) {
        return Arrays.stream(optionals)
                .map(Supplier::get)
                .filter(Optional::isPresent)
                .findFirst()
                .orElseGet(Optional::empty);
    }
}
