package com.pkb.common.testcontrol.services;

import com.pkb.common.testcontrol.message.FixTimeRequest;

public interface SetFixedTimestampService {
    void process(FixTimeRequest message);
}
