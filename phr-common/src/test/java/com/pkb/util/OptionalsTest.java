package com.pkb.util;

import static com.github.karsaig.approvalcrest.MatcherAssert.assertThat;
import static com.github.karsaig.approvalcrest.matcher.Matchers.sameBeanAs;

import java.util.Optional;

import org.junit.Test;

public class OptionalsTest {

    @Test
    public void testOptionalsOrReturnsFirstValueIfAllArePresent() {

        // GIVEN / WHEN
        Optional<String> actual = Optionals.or(
                () -> Optional.of("hello"),
                () -> Optional.of("optional"),
                () -> Optional.of("world")
        );

        // THEN
        assertThat(actual, sameBeanAs(Optional.of("hello")));
    }

    @Test
    public void testOptionalsOrReturnsFirstValueIfSomeArePresent() {

        // GIVEN / WHEN
        Optional<String> actual = Optionals.or(
                () -> Optional.empty(),
                () -> Optional.of("optional"),
                () -> Optional.of("world")
        );

        // THEN
        assertThat(actual, sameBeanAs(Optional.of("optional")));
    }

    @Test
    public void testOptionalsOrReturnsEmptyIfNoneArePresent() {

        // GIVEN / WHEN
        Optional<String> actual = Optionals.or(
                () -> Optional.empty(),
                () -> Optional.empty(),
                () -> Optional.empty()
        );

        // THEN
        assertThat(actual, sameBeanAs(Optional.empty()));
    }
}