package com.pkb.common.testcontrol.services;

import com.pkb.common.testcontrol.message.LogTestNameRequest;

public interface LogTestNameService {
    void process(LogTestNameRequest message);
}
