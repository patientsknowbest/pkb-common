package com.pkb.entities.enums;

import static com.pkb.entities.enums.NationalIdType.KVN_DE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class NationalIdTypeTest {

    @Test
    public void kvn_de_cleanInput_null() {
        assertNull(KVN_DE.cleanInput(null));
    }

    @Test
    public void kvn_de_cleanInput_empty() {
        assertNull(KVN_DE.cleanInput(""));
    }

    @Test
    public void kvn_de_cleanInput_middleSpaces() {
        assertEquals("FOOBAR", KVN_DE.cleanInput("foo   BAR  "));
    }


}
