package com.pkb.common.config;

import static com.github.karsaig.approvalcrest.jupiter.MatcherAssert.assertThat;
import static com.github.karsaig.approvalcrest.jupiter.MatcherAssert.assertThrows;
import static com.github.karsaig.approvalcrest.jupiter.matcher.Matchers.sameJsonAsApproved;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

//986a14
public class ImmutableRawConfigStorageTest {

    private ImmutableRawConfigStorage underTest = createStorage();

    private ImmutableRawConfigStorage createStorage() {
        Map<String, String> map = new HashMap<>();
        map.put("booleanKey", "true");
        map.put("booleanKeyWithSpaces", " true  ");
        map.put("uppercaseBooleanKey", "TRUE");
        map.put("booleanFalse","false");
        map.put("emptyKey", "");
        map.put("intKey", "2");
        map.put("longKey", "22");
        map.put("stringKey", "string value");
        return new ImmutableRawConfigStorage(map);
    }

    @Test
    public void getBoolean_success() {
        assertEquals(Boolean.TRUE, underTest.getBoolean("booleanKey"));
    }

    @Test
    public void getBooleanFalse_success() {
        assertEquals(Boolean.FALSE, underTest.getBoolean("booleanFalse"));
    }

    @Test
    public void getBoolean_caseInsensitiveSuccess() {
        assertEquals(Boolean.TRUE, underTest.getBoolean("uppercaseBooleanKey"));
    }

    @Test
    public void getBoolean_withSpaces_Success() {
        assertEquals(Boolean.TRUE, underTest.getBoolean("booleanKeyWithSpaces"));
    }

    @Test
    public void getBoolean_default_missingKey() {
        assertEquals(Boolean.FALSE, underTest.getBoolean("missingKey", Boolean.FALSE));
    }

    @Test
    public void getBoolean_missingKeyFailure() {
        //115b83
        assertThrows(sameJsonAsApproved(), () -> underTest.getBoolean("missingKey"));
    }

    @Test
    public void getBoolean_emptyKeyFailure() {
        //73c833
        assertThrows(sameJsonAsApproved(), () -> underTest.getBoolean("emptyKey"));
    }

    @Test
    public void getBoolean_malformedValueFailure() {
        //3d58a7
        assertThrows(sameJsonAsApproved(), () -> underTest.getBoolean("intKey"));
    }

    @Test
    public void getBoolean_withDefault_malformedValue() {
        //9e1034
        assertThrows(sameJsonAsApproved(), () -> underTest.getBoolean("intKey", true));
    }

    @Test
    public void getString_success() {
        assertEquals("string value", underTest.getString("stringKey"));
    }

    @Test
    public void getString_missingKey() {
        assertEquals(null, underTest.getString("missingKey"));
    }

    @Test
    public void getString_defaultNotUsed() {
        assertEquals("string value", underTest.getString("stringKey", "default string"));
    }

    @Test
    public void getString_defaultUsed() {
        assertEquals("default string", underTest.getString("missingKey", "default string"));
    }

    @Test
    public void getInt_success() {
        assertEquals(2, underTest.getInt("intKey"));
    }

    @Test
    public void getInt_missingKey() {
        //ea48dd
        assertThrows(sameJsonAsApproved(), () -> underTest.getInt("missingKey"));
    }

    @Test
    public void getInt_malformedValue() {
        //1b8e04
        assertThrows(sameJsonAsApproved(), () -> underTest.getInt("stringKey"));
    }

    @Test
    public void getInt_defaultUsed() {
        assertEquals(3, underTest.getInt("missingKey", 3));
    }

    @Test
    public void getInt_defaultNotUsed() {
        assertEquals(2, underTest.getInt("intKey", 3));
    }


    @Test
    public void getLong_success() {
        assertEquals(22, underTest.getLong("longKey"));
    }

    @Test
    public void getLong_missingKey() {
        //8ecb08
        assertThrows(sameJsonAsApproved(), () -> underTest.getLong("missingKey"));
    }

    @Test
    public void getLong_malformedValue() {
        //a5764f
        assertThrows(sameJsonAsApproved(), () -> underTest.getLong("stringKey"));
    }

    @Test
    public void getLong_defaultUsed() {
        assertEquals(3, underTest.getLong("missingKey", 3));
    }

    @Test
    public void getLong_defaultNotUsed() {
        assertEquals(22, underTest.getLong("longKey", 33));
    }

    @Test
    void mutableConfigIsFalseByDefault() {
        assertThat(underTest.isMutableConfigEnabled(), is(false));
    }

    @Test
    void mutableConfigReturnConfigValue() {
        //GIVEN
        Map<String, String> map = new HashMap<>();
        map.put("mutableConfig.enabled", "true");
        ImmutableRawConfigStorage sut = new ImmutableRawConfigStorage(map);
        //WHEN - THEN
        assertThat(sut.isMutableConfigEnabled(), is(true));
    }

    @Test
    void resetSetAndReSetDoesNothing() {
        //GIVEN
        String testedKey = "testedStringKey";
        String originalValue = "nonModifiableValue";
        Map<String, String> map = new HashMap<>();
        map.put(testedKey, originalValue);
        ImmutableRawConfigStorage sut = new ImmutableRawConfigStorage(map);
        //WHEN - THEN
        assertThat(sut.getString(testedKey), is(originalValue));
        //WHEN
        sut.setValue(testedKey, "NewValue");
        //THEN
        assertThat(sut.getString(testedKey), is(originalValue));
        //WHEN
        sut.reset();
        //THEN
        assertThat(sut.getString(testedKey), is(originalValue));
    }

    @Test
    void getImmutableConfigReturnsSelf() {
        ConfigStorage configStorage = underTest.getImmutableConfig();
        assertThat(configStorage, sameInstance(underTest));
    }
}
