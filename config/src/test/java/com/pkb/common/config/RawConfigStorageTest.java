package com.pkb.common.config;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class RawConfigStorageTest {

    private RawConfigStorage createStorage() {
        Map<String, String> map = new HashMap<>();
        map.put("booleanKey", "true");
        map.put("uppercaseBooleanKey", "TRUE");
        map.put("emptyKey", "");
        map.put("intKey", "2");
        map.put("longKey", "22");
        map.put("stringKey", "string value");
        return new RawConfigStorage(map);
    }

    @Test
    public void getBoolean_success() {
        assertEquals(Boolean.TRUE, createStorage().getBoolean("booleanKey"));
    }

    @Test
    public void getBoolean_caseInsensitiveSuccess() {
        assertEquals(Boolean.TRUE, createStorage().getBoolean("uppercaseBooleanKey"));
    }

    @Test
    public void getBoolean_default_missingKey() {
        assertEquals(Boolean.FALSE, createStorage().getBoolean("missingKey", Boolean.FALSE));
    }

    @Test(expected = MissingValueException.class)
    public void getBoolean_missingKeyFailure() {
        createStorage().getBoolean("missingKey");
    }

    @Test(expected = MalformedValueException.class)
    public void getBoolean_emptyKeyFailure() {
        createStorage().getBoolean("emptyKey");
    }

    @Test(expected = MalformedValueException.class)
    public void getBoolean_malformedValueFailure() {
        createStorage().getBoolean("intKey");
    }

    @Test(expected = MalformedValueException.class)
    public void getBoolean_withDefault_malformedValue() {
        createStorage().getBoolean("intKey", true);
    }

    @Test
    public void getString_success() {
        assertEquals("string value", createStorage().getString("stringKey"));
    }

    @Test
    public void getString_missingKey() {
        assertEquals(null, createStorage().getString("missingKey"));
    }

    @Test
    public void getString_defaultNotUsed() {
        assertEquals("string value", createStorage().getString("stringKey", "default string"));
    }

    @Test
    public void getString_defaultUsed() {
        assertEquals("default string", createStorage().getString("missingKey", "default string"));
    }

    @Test
    public void getInt_success() {
        assertEquals(2, createStorage().getInt("intKey"));
    }

    @Test(expected = MissingValueException.class)
    public void getInt_missingKey() {
        createStorage().getInt("missingKey");
    }

    @Test(expected = MalformedValueException.class)
    public void getInt_malformedValue() {
        createStorage().getInt("stringKey");
    }

    @Test
    public void getInt_defaultUsed() {
        assertEquals(3, createStorage().getInt("missingKey", 3));
    }

    @Test
    public void getInt_defaultNotUsed() {
        assertEquals(2, createStorage().getInt("intKey", 3));
    }


    @Test
    public void getLong_success() {
        assertEquals(22, createStorage().getLong("longKey"));
    }

    @Test(expected = MissingValueException.class)
    public void getLong_missingKey() {
        createStorage().getLong("missingKey");
    }

    @Test(expected = MalformedValueException.class)
    public void getLong_malformedValue() {
        createStorage().getLong("stringKey");
    }

    @Test
    public void getLong_defaultUsed() {
        assertEquals(3, createStorage().getLong("missingKey", 3));
    }

    @Test
    public void getLong_defaultNotUsed() {
        assertEquals(22, createStorage().getLong("longKey", 33));
    }



}
