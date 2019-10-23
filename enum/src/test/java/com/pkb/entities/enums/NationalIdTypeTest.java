package com.pkb.entities.enums;

import static com.pkb.entities.enums.NationalIdType.KVN_DE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.pkb.testing.util.EnumTestHelper;

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

    /**
     * Duplicated enum attributes are currently unexpected
     */
    @Test
    public void checkNoDuplicateNames() {
        EnumTestHelper.ensureEnumValueIsUnique("Found duplicate Names ", NationalIdType.class, NationalIdType::getName);
    }
    @Test
    public void checkNoDuplicateCountries() {
        EnumTestHelper.ensureEnumValueIsUnique("Found duplicate Countries ", NationalIdType.class, NationalIdType::getCountryCodes);
    }
    @Test
    public void checkNoDuplicateAuthority() {
        EnumTestHelper.ensureEnumValueIsUnique("Found duplicate Authorities ", NationalIdType.class, NationalIdType::getHl7IdAuthority);
    }
    @Test
    public void checkNoDuplicateFhirIdentifier() {
        EnumTestHelper.ensureEnumValueIsUniqueWithFilters("Found duplicate FhirIdentifier ", NationalIdType.class, NationalIdType::getFhirIdentifierSystem, it -> {
            //null fhir identifiers are expected currently
            return it.getFhirIdentifierSystem() != null;
        });
    }

}
