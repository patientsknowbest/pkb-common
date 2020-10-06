package com.pkb.entities.enums;

import org.junit.jupiter.api.Test;

import com.pkb.testing.util.EnumTestHelper;

public class MaritalStatusTest {

    /**
     * Duplicated enum codes are currently unexpected
     */
    @Test
    public void checkNoDuplicateCodes() {
        EnumTestHelper.ensureEnumValueIsUnique("Found duplicate codes ", MaritalStatus.class, MaritalStatus::getCode);
    }

}