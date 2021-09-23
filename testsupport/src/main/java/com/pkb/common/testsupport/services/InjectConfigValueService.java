package com.pkb.common.testsupport.services;

import com.pkb.pubsub.testsupport.payload.InjectConfigRequest;
import com.pkb.pubsub.testsupport.payload.InjectConfigResponse;

public interface InjectConfigValueService {
    InjectConfigResponse process(InjectConfigRequest message);
}
