package com.pkb.common.testsupport.services;

import com.pkb.pubsub.testsupport.payload.LogTestNameRequest;
import com.pkb.pubsub.testsupport.payload.LogTestNameResponse;

public interface LogTestNameService {
    LogTestNameResponse process(LogTestNameRequest message);
}
