package com.pkb.common.testcontrol.message;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableLogTestNameRequest.class)
@JsonDeserialize(as = ImmutableLogTestNameRequest.class)
public interface LogTestNameRequest {
    String testName();
}
