package com.pkb.common.testcontrol.services;

import com.pkb.common.testcontrol.message.MoveTimeRequest;

public interface MoveTimeService {
    void process(MoveTimeRequest message);
}
