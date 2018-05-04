package com.pkb.common.config;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class RawConfigStorageTest {

    private RawConfigStorage createStorage() {
        Map<String, String> map = new HashMap<>();
        map.put("booleanKey", "true");
        map.put("booleanKeyWithSpaces", " true  ");
        map.put("uppercaseBooleanKey", "TRUE");
        map.put("emptyKey", "");
        map.put("intKey", "2");
        map.put("longKey", "22");
        map.put("stringKey", "string value");
        return new RawConfigStorage(map);
    }

    @Rule
    public ExpectedException exc = ExpectedException.none();

    @Test
    public void getBoolean_success() {
        assertEquals(Boolean.TRUE, createStorage().getBoolean("booleanKey"));
    }

    @Test
    public void getBoolean_caseInsensitiveSuccess() {
        assertEquals(Boolean.TRUE, createStorage().getBoolean("uppercaseBooleanKey"));
    }

    @Test
    public void getBoolean_withSpaces_Success() {
        assertEquals(Boolean.TRUE, createStorage().getBoolean("booleanKeyWithSpaces"));
    }

    @Test
    public void getBoolean_default_missingKey() {
        assertEquals(Boolean.FALSE, createStorage().getBoolean("missingKey", Boolean.FALSE));
    }

    @Test(expected = MissingValueException.class)
    public void getBoolean_missingKeyFailure() {
        createStorage().getBoolean("missingKey");
    }

    @Test
    public void getBoolean_emptyKeyFailure() {
        exc.expect(MalformedValueException.class);
        exc.expectMessage("malformed value=[] for configuration key=[emptyKey] (expected type=[Boolean])");

        createStorage().getBoolean("emptyKey");
    }

    @Test
    public void getBoolean_malformedValueFailure() {
        exc.expect(MalformedValueException.class);
        exc.expectMessage("malformed value=[2] for configuration key=[intKey] (expected type=[Boolean])");

        createStorage().getBoolean("intKey");
    }

    @Test
    public void getBoolean_withDefault_malformedValue() {
        exc.expect(MalformedValueException.class);
        exc.expectMessage("malformed value=[2] for configuration key=[intKey] (expected type=[Boolean])");

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

    @Test
    public void getInt_malformedValue() {
        exc.expect(MalformedValueException.class);
        exc.expectMessage("malformed value=[string value] for configuration key=[stringKey] (expected type=[Integer])");

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

    @Test
    public void getLong_malformedValue() {
        exc.expect(MalformedValueException.class);
        exc.expectMessage("malformed value=[string value] for configuration key=[stringKey] (expected type=[Long])");

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
