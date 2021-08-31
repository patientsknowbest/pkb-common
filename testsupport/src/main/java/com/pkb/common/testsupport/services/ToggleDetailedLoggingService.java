package com.pkb.common.testsupport.services;

import com.pkb.pubsub.testsupport.payload.ToggleDetailedLoggingRequest;
import com.pkb.pubsub.testsupport.payload.ToggleDetailedLoggingResponse;

public interface ToggleDetailedLoggingService {
    ToggleDetailedLoggingResponse process(ToggleDetailedLoggingRequest message);
}
