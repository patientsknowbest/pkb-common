package com.pkb.common.testcontrol.message;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableClearStatesRequest.class)
@JsonDeserialize(as = ImmutableClearStatesRequest.class)
public interface InjectConfigRequest {
    String key();
    String value();
}
