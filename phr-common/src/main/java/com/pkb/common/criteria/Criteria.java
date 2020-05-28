package com.pkb.common.criteria;

import org.immutables.value.Value;

public interface Criteria {

    @Value.Default
    default int getPageSize() {
        return 50;
    }
}
