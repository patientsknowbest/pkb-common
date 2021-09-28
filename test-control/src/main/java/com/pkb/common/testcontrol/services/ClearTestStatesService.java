package com.pkb.common.testcontrol.services;

import com.pkb.common.testcontrol.message.ClearStatesRequest;

public interface ClearTestStatesService {
    void process(ClearStatesRequest message);
}
