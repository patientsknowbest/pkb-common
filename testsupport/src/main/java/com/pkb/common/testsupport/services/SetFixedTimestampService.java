package com.pkb.common.testsupport.services;

import com.pkb.pubsub.testsupport.payload.SetFixedTimestampRequest;
import com.pkb.pubsub.testsupport.payload.SetFixedTimestampResponse;

public interface SetFixedTimestampService {
    SetFixedTimestampResponse process(SetFixedTimestampRequest message);
}
