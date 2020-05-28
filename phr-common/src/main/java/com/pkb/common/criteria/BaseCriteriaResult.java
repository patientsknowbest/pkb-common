package com.pkb.common.criteria;

import java.util.List;

import org.immutables.value.Value;

import com.google.common.base.Preconditions;

public interface BaseCriteriaResult<I> {
    List<I> getPage();

    @Value.Parameter
    long getTotal();

    @Value.Check
    default void checkConsistency() {
        Preconditions.checkState(getPage().size() <= getTotal(), "Current page contains more item, than total");
    }
}
