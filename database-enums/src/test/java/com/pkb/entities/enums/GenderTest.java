package com.pkb.entities.enums;

import org.junit.Test;

import com.pkb.testing.util.EnumTestHelper;

public class GenderTest {

    /**
     * Duplicated enum attributes are currently unexpected
     */
    @Test
    public void checkNoDuplicateCodes() {
        EnumTestHelper.ensureEnumValueIsUnique("Found duplicate codes ", Gender.class, Gender::getCode);
    }
    @Test
    public void checkNoDuplicateIds() {
        EnumTestHelper.ensureEnumValueIsUnique("Found duplicate ids ", Gender.class, Gender::getId);
    }

}