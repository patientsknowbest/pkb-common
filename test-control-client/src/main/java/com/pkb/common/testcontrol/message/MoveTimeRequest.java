package com.pkb.common.testcontrol.message;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableMoveTimeRequest.class)
@JsonDeserialize(as = ImmutableMoveTimeRequest.class)
public interface MoveTimeRequest {
    long amount();
    String unit();
}
