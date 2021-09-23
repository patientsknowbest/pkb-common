package com.pkb.common.testsupport.services;

import com.pkb.pubsub.testsupport.payload.ClearTestStatesRequest;
import com.pkb.pubsub.testsupport.payload.ClearTestStatesResponse;

public interface ClearTestStatesService {
    ClearTestStatesResponse process(ClearTestStatesRequest message);
}
