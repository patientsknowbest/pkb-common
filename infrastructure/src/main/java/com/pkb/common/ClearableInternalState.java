package com.pkb.common;

/**
 * Interface to implement by classes which has internal state, which should be cleared between test runs.
 */
public interface ClearableInternalState {

    void clearState();
}
