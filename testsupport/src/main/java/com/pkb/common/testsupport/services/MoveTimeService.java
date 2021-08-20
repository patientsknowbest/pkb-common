package com.pkb.common.testsupport.services;

import com.pkb.pubsub.testsupport.payload.MoveTimeRequest;
import com.pkb.pubsub.testsupport.payload.MoveTimeResponse;

public interface MoveTimeService {
    MoveTimeResponse process(MoveTimeRequest message);
}
