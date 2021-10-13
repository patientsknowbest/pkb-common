package com.pkb.common.testcontrol.services;

import com.pkb.common.testcontrol.message.ClearInternalStateRequest;

/**
 * Implementations should clear all _caches_ i.e. ephemeral storage which can safely be discarded
 */
public interface ClearInternalStateService {
    void process(ClearInternalStateRequest message);
}
