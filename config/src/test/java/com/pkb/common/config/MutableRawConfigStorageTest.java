package com.pkb.common.config;

import static com.github.karsaig.approvalcrest.jupiter.MatcherAssert.assertThat;
import static com.github.karsaig.approvalcrest.jupiter.MatcherAssert.assertThrows;
import static com.github.karsaig.approvalcrest.jupiter.matcher.Matchers.sameJsonAsApproved;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

//84fe55
class MutableRawConfigStorageTest {

    private ImmutableRawConfigStorage immutableRawConfigStorage = createStorage();

    private ImmutableRawConfigStorage createStorage() {
        Map<String, String> map = new HashMap<>();
        map.put("booleanKey", "true");
        map.put("booleanKeyWithSpaces", " true  ");
        map.put("uppercaseBooleanKey", "TRUE");
        map.put("booleanFalse", "false");
        map.put("emptyKey", "");
        map.put("intKey", "2");
        map.put("longKey", "22");
        map.put("stringKey", "string value");
        return new ImmutableRawConfigStorage(map);
    }

    private MutableRawConfigStorage underTest = new MutableRawConfigStorage(immutableRawConfigStorage);

    @DisplayName("get string returns original value without override")
    @Test
    public void getString1() {
        assertEquals("string value", underTest.getString("stringKey"));
    }

    @DisplayName("get string returns overridden value with override")
    @Test
    public void getString2() {
        underTest.setValue("stringKey", "overridden value");
        assertEquals("overridden value", underTest.getString("stringKey"));
    }

    @DisplayName("get string returns null for not existing key")
    @Test
    public void getString3() {
        assertEquals(null, underTest.getString("notExistingKey"));
    }

    @DisplayName("get string returns default value for not existing key with default given")
    @Test
    public void getString4() {
        assertEquals("default value", underTest.getString("notExistingKey", "default value"));
    }

    @DisplayName("get string returns overridden value even for not existing key")
    @Test
    public void getString5() {
        underTest.setValue("notExistingKey", "overridden value");
        assertEquals("overridden value", underTest.getString("notExistingKey"));
    }

    @DisplayName("get string returns overridden value even for not existing key with default given")
    @Test
    public void getString6() {
        underTest.setValue("notExistingKey", "overridden value");
        assertEquals("overridden value", underTest.getString("notExistingKey", "default value"));
    }

    @DisplayName("get string returns null value with override set to null")
    @Test
    public void getString7() {
        underTest.setValue("stringKey", null);
        assertEquals(null, underTest.getString("stringKey"));
    }

    @DisplayName("get string returns original value after override is reset")
    @Test
    public void getString8() {
        //GIVEN
        underTest.setValue("stringKey", "overridden value");
        //WHEN - THEN
        assertEquals("overridden value", underTest.getString("stringKey", "default value"));
        //WHEN
        underTest.reset();
        //THEN
        assertEquals("string value", underTest.getString("stringKey", "default value"));
    }

    @DisplayName("getBoolean returns original value without override")
    @Test
    public void getBoolean1() {
        assertEquals(true, underTest.getBoolean("booleanKey"));
    }

    @DisplayName("getBoolean returns overridden value with override")
    @Test
    public void getBoolean2() {
        underTest.setValue("booleanKey", "false");
        assertEquals(false, underTest.getBoolean("booleanKey"));
    }

    @DisplayName("getBoolean throw exception when key not exists")
    @Test
    public void getBoolean3() {
        //c06594
        assertThrows(sameJsonAsApproved(), () -> underTest.getBoolean("notExistingKey"));
    }

    @DisplayName("getBoolean returns default value for not existing key with default given")
    @Test
    public void getBoolean4() {
        assertEquals(false, underTest.getBoolean("notExistingKey", false));
    }

    @DisplayName("getBoolean returns overridden value even for not existing key")
    @Test
    public void getBoolean5() {
        underTest.setValue("notExistingKey", "true");
        assertEquals(true, underTest.getBoolean("notExistingKey"));
    }

    @DisplayName("getBoolean returns overridden value even for not existing key with default given")
    @Test
    public void getBoolean6() {
        underTest.setValue("notExistingKey", "false");
        assertEquals(false, underTest.getBoolean("notExistingKey", true));
    }

    @DisplayName("getBoolean throws exception with override set to null")
    @Test
    public void getBoolean7() {
        underTest.setValue("booleanKey", null);
        assertThrows(sameJsonAsApproved(), () -> underTest.getBoolean("booleanKey"));
    }

    @DisplayName("getBoolean throw exception when original value is not Boolean")
    @Test
    public void getBoolean8() {
        //2d20d5
        assertThrows(sameJsonAsApproved(), () -> underTest.getBoolean("stringKey"));
    }

    @DisplayName("getBoolean throws exception when overridden value is not Boolean")
    @Test
    public void getBoolean9() {
        underTest.setValue("booleanKey", "not a boolean");
        assertThrows(sameJsonAsApproved(), () -> underTest.getBoolean("booleanKey"));
    }

    @DisplayName("getBoolean throw exception when overridden value and original value are not Boolean values")
    @Test
    public void getBoolean10() {
        underTest.setValue("stringKey", "not a boolean");
        //e9f610
        assertThrows(sameJsonAsApproved(), () -> underTest.getBoolean("stringKey"));
    }

    @DisplayName("getBoolean throw exception when overridden value and original value are not Boolean values and default given")
    @Test
    public void getBoolean11() {
        underTest.setValue("stringKey", "not a boolean");
        //a42bd3
        assertThrows(sameJsonAsApproved(), () -> underTest.getBoolean("stringKey", true));
    }

    @DisplayName("getBoolean returns original value after override is reset")
    @Test
    public void getBoolean12() {
        //GIVEN
        underTest.setValue("booleanKey", "false");
        //WHEN - THEN
        assertEquals(false, underTest.getBoolean("booleanKey"));
        //WHEN
        underTest.reset();
        //THEN
        assertEquals(true, underTest.getBoolean("booleanKey"));
    }

    @Test
    void mutableConfigIsTrueByDefault() {
        assertThat(underTest.isMutableConfigEnabled(), is(true));
    }

    @Test
    void mutableConfigCannotBeOverridden() {
        //GIVEN
        underTest.setValue("mutableConfig.enabled", "false");
        //WHEN - THEN
        assertThat(underTest.isMutableConfigEnabled(), is(true));
    }


    @DisplayName("getInt returns original value without override")
    @Test
    public void getInt1() {
        assertEquals(2, underTest.getInt("intKey"));
    }

    @DisplayName("getInt returns overridden value with override")
    @Test
    public void getInt2() {
        underTest.setValue("intKey", "3");
        assertEquals(3, underTest.getInt("intKey"));
    }

    @DisplayName("getInt throw exception when key not exists")
    @Test
    public void getInt3() {
        //6bd26f
        assertThrows(sameJsonAsApproved(), () -> underTest.getInt("notExistingKey"));
    }

    @DisplayName("getInt returns default value for not existing key with default given")
    @Test
    public void getInt4() {
        assertEquals(4, underTest.getInt("notExistingKey", 4));
    }

    @DisplayName("getInt returns overridden value even for not existing key")
    @Test
    public void getInt5() {
        underTest.setValue("notExistingKey", "5");
        assertEquals(5, underTest.getInt("notExistingKey"));
    }

    @DisplayName("getInt returns overridden value even for not existing key with default given")
    @Test
    public void getInt6() {
        underTest.setValue("notExistingKey", "6");
        assertEquals(6, underTest.getInt("notExistingKey", 7));
    }

    @DisplayName("getInt throws exception with override set to null")
    @Test
    public void getInt7() {
        underTest.setValue("intKey", null);
        assertThrows(sameJsonAsApproved(), () -> underTest.getInt("intKey"));
    }

    @DisplayName("getInt throw exception when original value is not Integer")
    @Test
    public void getInt8() {
        //6bf9d8
        assertThrows(sameJsonAsApproved(), () -> underTest.getInt("stringKey"));
    }

    @DisplayName("getInt throws exception when overridden value is not Integer")
    @Test
    public void getInt9() {
        underTest.setValue("intKey", "not an integer");
        assertThrows(sameJsonAsApproved(), () -> underTest.getInt("intKey"));
    }

    @DisplayName("getInt throw exception when overridden value and original value are not Integers values")
    @Test
    public void getInt10() {
        underTest.setValue("stringKey", "not an integer");
        //b44a6f
        assertThrows(sameJsonAsApproved(), () -> underTest.getInt("stringKey"));
    }

    @DisplayName("getInt returns default value when overridden value and original value are not Integers values and default given")
    @Test
    public void getInt11() {
        underTest.setValue("stringKey", "not an integer");
        //a42bd3
        assertEquals(13, underTest.getInt("stringKey", 13));
    }

    @DisplayName("getInt returns original value after override is reset")
    @Test
    public void getInt12() {
        //GIVEN
        underTest.setValue("intKey", "13");
        //WHEN - THEN
        assertEquals(13, underTest.getInt("intKey"));
        //WHEN
        underTest.reset();
        //THEN
        assertEquals(2, underTest.getInt("intKey"));
    }

    @DisplayName("getInt returns original value with override deleted")
    @Test
    public void getInt13() {
        underTest.setValue("intKey", "14");
        OverrideRemovalResult removalResult = underTest.removeOverrideAtKey("intKey");
        assertEquals(OverrideRemovalResult.REMOVED, removalResult);
        assertEquals(2, underTest.getInt("intKey"));
    }

    @DisplayName("removing non-existent key returns NOT_FOUND")
    @Test
    public void removingNonExistentKey() {
        OverrideRemovalResult removalResult = underTest.removeOverrideAtKey("x");
        assertEquals(OverrideRemovalResult.KEY_NOT_FOUND, removalResult);
    }

    @DisplayName("getLong returns original value without override")
    @Test
    public void getLong1() {
        assertEquals(22, underTest.getLong("longKey"));
    }

    @DisplayName("getLong returns overridden value with override")
    @Test
    public void getLong2() {
        underTest.setValue("longKey", "33");
        assertEquals(33, underTest.getLong("longKey"));
    }

    @DisplayName("getLong throw exception when key not exists")
    @Test
    public void getLong3() {
        //61b3a9
        assertThrows(sameJsonAsApproved(), () -> underTest.getLong("notExistingKey"));
    }

    @DisplayName("getLong returns default value for not existing key with default given")
    @Test
    public void getLong4() {
        assertEquals(4, underTest.getLong("notExistingKey", 4));
    }
}