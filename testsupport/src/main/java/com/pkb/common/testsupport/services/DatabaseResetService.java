package com.pkb.common.testsupport.services;

import com.pkb.pubsub.testsupport.payload.ResetDatabaseRequest;
import com.pkb.pubsub.testsupport.payload.ResetDatabaseResponse;

public interface DatabaseResetService {

    ResetDatabaseResponse process(ResetDatabaseRequest resetDatabaseRequest);

}
