package com.pkb.common.testsupport.services;

import com.pkb.pubsub.testsupport.payload.ResetDatabaseRequest;
import com.pkb.pubsub.testsupport.payload.ResetDatabaseResponse;

public class DefaultDatabaseResetService implements DatabaseResetService {
    @Override
    public ResetDatabaseResponse process(ResetDatabaseRequest resetDatabaseRequest) {
        return ResetDatabaseResponse.newBuilder().build();
    }
}
