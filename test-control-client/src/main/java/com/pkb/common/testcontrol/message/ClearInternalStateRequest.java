package com.pkb.common.testcontrol.message;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableClearInternalStateRequest.class)
@JsonDeserialize(as = ImmutableClearInternalStateRequest.class)
public interface ClearInternalStateRequest {
    boolean clearFixedTimestamp();
}
