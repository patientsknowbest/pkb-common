package com.pkb.common.testcontrol.services;

import com.pkb.common.testcontrol.message.DetailedLoggingRequest;

public interface ToggleDetailedLoggingService {
    void process(DetailedLoggingRequest message);
}
