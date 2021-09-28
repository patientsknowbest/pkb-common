package com.pkb.common.testcontrol.message;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;
import org.jetbrains.annotations.Nullable;

@Value.Immutable
@JsonSerialize(as = ImmutableInjectConfigRequest.class)
@JsonDeserialize(as = ImmutableInjectConfigRequest.class)
public interface InjectConfigRequest {
    String key();
    
    // Nullable to allow clearing a config override.
    // Optional required additional configuration of Jackson ObjectMapper
    // which is inconvenient while we're using different dependency injection
    // frameworks still. Use @Nullable instead for now
    @Nullable String value();
}
