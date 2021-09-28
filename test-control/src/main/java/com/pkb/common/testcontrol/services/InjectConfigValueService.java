package com.pkb.common.testcontrol.services;

import com.pkb.common.testcontrol.message.InjectConfigRequest;

public interface InjectConfigValueService {
    void process(InjectConfigRequest message);
}
