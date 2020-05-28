package com.pkb.common.criteria;

import static com.pkb.common.criteria.ImmutableRestriction.restriction;

import java.time.temporal.Temporal;
import java.util.Optional;

import org.immutables.value.Value;
import org.immutables.value.Value.Derived;
import org.immutables.value.Value.Style;

@Value.Immutable
@Style(of = "temporalRangeRestriction")
public interface TemporalRangeRestriction<T extends Temporal> {

    @Value.Parameter(order = 1)
    Optional<Restriction<LowerBound, T>> getLowerBound();

    @Value.Parameter(order = 2)
    Optional<Restriction<UpperBound, T>> getUpperBound();

    @Value.Parameter(order = 3)
    Optional<T> getEquality();

    @Derived
    default boolean isNonEmpty() {
        return getLowerBound().isPresent() || getUpperBound().isPresent() || getEquality().isPresent();
    }

    static <T extends Temporal> TemporalRangeRestriction<T> equalToWithBounds(T temporal) {
        return ImmutableTemporalRangeRestriction.<T>builder()
                .lowerBound(restriction(LowerBound.GREATER_OR_EQUAL,temporal))
                .upperBound(restriction(UpperBound.LESS_OR_EQUAL, temporal))
                .build();
    }

    static <T extends Temporal> TemporalRangeRestriction<T> equalToWithEquality(T temporal) {
        return ImmutableTemporalRangeRestriction.<T>builder()
                .equality(temporal)
                .build();
    }

    static <T extends Temporal> TemporalRangeRestriction<T> unbounded() {
        return ImmutableTemporalRangeRestriction.<T> builder().build();
    }
}
