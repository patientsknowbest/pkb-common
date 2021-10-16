package com.pkb.common.testcontrol.services;

import com.pkb.common.testcontrol.message.ClearStorageRequest;

/**
 * Implementations should clear all persistent storages 
 * i.e. databases, filesystems, cloud storage buckets.
 */
public interface ClearStorageService {
    void process(ClearStorageRequest request);
}
