package com.pkb.common.testcontrol.services;

import com.pkb.common.testcontrol.message.ResumeProcessingRequest;
import com.pkb.common.testcontrol.message.SuspendProcessingRequest;

/**
 * Implementations should supply means to
 * - suspend any processing (e.g. suspend camel routes, stop any processing of external resources, like database changes)
 * - resume previously suspended processing
 */
public interface ProcessingControllerService {
    void process(ResumeProcessingRequest message);
    void process(SuspendProcessingRequest message);
}
