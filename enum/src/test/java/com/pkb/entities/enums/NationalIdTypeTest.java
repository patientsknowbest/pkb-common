package com.pkb.entities.enums;

import static com.github.karsaig.approvalcrest.MatcherAssert.assertThat;
import static com.github.karsaig.approvalcrest.matcher.Matchers.sameBeanAs;
import static com.pkb.entities.enums.NationalIdType.*;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.*;

import com.google.common.collect.ImmutableSet;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;

import com.pkb.testing.util.EnumTestHelper;
import org.junit.runner.RunWith;

import java.util.Set;
import java.util.stream.Stream;

@RunWith(DataProviderRunner.class)
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
    @Test
    public void testFromName_NHS_NUMBER() {
        NationalIdType type = NationalIdType.getNationalIdTypeFromName("NHS number");
        assertEquals(NHS_NUMBER, type);
    }

    @Test
    public void testIsValid_NHS_NUMBER_null() {
        assertFalse(NHS_NUMBER.isValid(null));
    }

    @Test
    public void testIsValid_NHS_NUMBER_empty() {
        assertFalse(NHS_NUMBER.isValid(""));
    }

    @Test
    public void testIsValid_NHS_NUMBER_blank() {
        assertFalse(NHS_NUMBER.isValid("    "));
    }

    @Test
    public void testIsValid_NHS_NUMBER_text() {
        assertFalse(NHS_NUMBER.isValid("wibble"));
    }

    @Test
    public void testIsValid_NHS_NUMBER_short() {
        assertFalse(NHS_NUMBER.isValid("923447570"));
    }

    @Test
    public void testIsValid_NHS_NUMBER_long() {
        assertFalse(NHS_NUMBER.isValid("92344757040"));
    }

    @Test
    public void testIsValid_NHS_NUMBER_invalid_checksum() {
        assertFalse(NHS_NUMBER.isValid("9234475705"));
    }

    @Test
    public void testIsValid_NHS_NUMBER_not_clean_start_space() {
        assertFalse(NHS_NUMBER.isValid(" 9234475705"));
    }

    @Test
    public void testIsValid_NHS_NUMBER_not_clean_end_space() {
        assertFalse(NHS_NUMBER.isValid("9234475705 "));
    }

    @Test
    public void testIsValid_NHS_NUMBER_valid() {
        assertTrue(NHS_NUMBER.isValid("9234475704"));
    }

    @Test
    public void testCleanInput_NHS_NUMBER_already_clean() {
        assertEquals("9234475704", NHS_NUMBER.cleanInput("9234475704"));
    }

    @Test
    public void testCleanInput_NHS_NUMBER_start_space() {
        assertEquals("9234475704", NHS_NUMBER.cleanInput(" 9234475704"));
    }

    @Test
    public void testCleanInput_NHS_NUMBER_end_space() {
        assertEquals("9234475704", NHS_NUMBER.cleanInput("9234475704 "));
    }

    @Test
    public void testCleanInput_NHS_NUMBER_whitespace() {
        assertEquals("9234475704", NHS_NUMBER.cleanInput("923 447 5704"));
    }

    // CHI_NUMBER

    @Test
    public void testFromName_CHI_NUMBER() {
        NationalIdType type = NationalIdType.getNationalIdTypeFromName("CHI number");
        assertEquals(CHI_NUMBER, type);
    }

    @Test
    public void testIsValid_CHI_NUMBER_null() {
        assertFalse(CHI_NUMBER.isValid(null));
    }

    @Test
    public void testIsValid_CHI_NUMBER_empty() {
        assertFalse(CHI_NUMBER.isValid(""));
    }

    @Test
    public void testIsValid_CHI_NUMBER_blank() {
        assertFalse(CHI_NUMBER.isValid("    "));
    }

    @Test
    public void testIsValid_CHI_NUMBER_text() {
        assertFalse(CHI_NUMBER.isValid("wibble"));
    }

    @Test
    public void testIsValid_CHI_NUMBER_valid() {
        assertTrue(CHI_NUMBER.isValid("2808319215"));
    }

    @Test
    public void testCleanInput_CHI_NUMBER_already_clean() {
        assertEquals("2808319215", CHI_NUMBER.cleanInput("2808319215"));
    }

    @Test
    public void testCleanInput_CHI_NUMBER_start_space() {
        assertEquals("2808319215", CHI_NUMBER.cleanInput(" 2808319215"));
    }

    @Test
    public void testCleanInput_CHI_NUMBER_end_space() {
        assertEquals("2808319215", CHI_NUMBER.cleanInput("2808319215 "));
    }

    // H_AND_C_NUMBER

    @Test
    public void testFromName_H_AND_C_NUMBER() {
        NationalIdType type = NationalIdType.getNationalIdTypeFromName("Health and Care number");
        assertEquals(H_AND_C_NUMBER, type);
    }

    @Test
    public void testIsValid_H_AND_C_NUMBER_null() {
        assertFalse(H_AND_C_NUMBER.isValid(null));
    }

    @Test
    public void testIsValid_H_AND_C_NUMBER_empty() {
        assertFalse(H_AND_C_NUMBER.isValid(""));
    }

    @Test
    public void testIsValid_H_AND_C_NUMBER_blank() {
        assertFalse(H_AND_C_NUMBER.isValid("    "));
    }

    @Test
    public void testIsValid_H_AND_C_NUMBER_text() {
        assertFalse(H_AND_C_NUMBER.isValid("wibble"));
    }

    @Test
    public void testIsValid_H_AND_C_NUMBER_valid() {
        assertTrue(H_AND_C_NUMBER.isValid("3795016940"));
    }

    @Test
    public void testCleanInput_H_AND_C_NUMBER_already_clean() {
        assertEquals("3795016940", H_AND_C_NUMBER.cleanInput("3795016940"));
    }

    @Test
    public void testCleanInput_H_AND_C_NUMBER_start_space() {
        assertEquals("3795016940", H_AND_C_NUMBER.cleanInput(" 3795016940"));
    }

    @Test
    public void testCleanInput_H_AND_C_NUMBER_end_space() {
        assertEquals("3795016940", H_AND_C_NUMBER.cleanInput("3795016940 "));
    }

    // IHI_NUMBER

    @Test
    public void testFromName_IHI_NUMBER() {
        NationalIdType type = NationalIdType.getNationalIdTypeFromName("IHI number");
        assertEquals(IHI_NUMBER, type);
    }

    static final String VALID_CLEAN_IHI_NUMBER = "1234567890";

    @Test
    public void testIsValid_IHI_NUMBER_null() {
        assertFalse(IHI_NUMBER.isValid(null));
    }

    @Test
    public void testIsValid_IHI_NUMBER_empty() {
        assertFalse(IHI_NUMBER.isValid(""));
    }

    @Test
    public void testIsValid_IHI_NUMBER_blank() {
        assertFalse(IHI_NUMBER.isValid("    "));
    }

    @Test
    public void testIsValid_IHI_NUMBER_text() {
        assertFalse(IHI_NUMBER.isValid("wibble"));
    }

    @Test
    public void testIsValid_IHI_NUMBER_valid() {
        assertTrue(IHI_NUMBER.isValid(VALID_CLEAN_IHI_NUMBER));
    }

    @Test
    public void testCleanInput_IHI_NUMBER_already_clean() {
        assertEquals(VALID_CLEAN_IHI_NUMBER, IHI_NUMBER.cleanInput(VALID_CLEAN_IHI_NUMBER));
    }

    @Test
    public void testCleanInput_IHI_NUMBER_start_space() {
        assertEquals(VALID_CLEAN_IHI_NUMBER, IHI_NUMBER.cleanInput(" " + VALID_CLEAN_IHI_NUMBER));
    }

    @Test
    public void testCleanInput_IHI_NUMBER_end_space() {
        assertEquals(VALID_CLEAN_IHI_NUMBER, IHI_NUMBER.cleanInput(VALID_CLEAN_IHI_NUMBER + " "));
    }

    // SSN_USA

    @Test
    public void testFromName_SSN_USA() {
        NationalIdType type = NationalIdType.getNationalIdTypeFromName("SSN");
        assertEquals(SSN_USA, type);
    }

    @Test
    public void testIsValid_SSN_USA_null() {
        assertFalse(SSN_USA.isValid(null));
    }

    @Test
    public void testIsValid_SSN_USA_empty() {
        assertFalse(SSN_USA.isValid(""));
    }

    @Test
    public void testIsValid_SSN_USA_blank() {
        assertFalse(SSN_USA.isValid("    "));
    }

    @Test
    public void testIsValid_SSN_USA_text() {
        assertFalse(SSN_USA.isValid("wibble"));
    }

    @Test
    public void testIsValid_SSN_USA_valid() {
        assertTrue(SSN_USA.isValid("011188360"));
    }

    @Test
    public void testCleanInput_SSN_USA_already_clean() {
        assertEquals("011188360", SSN_USA.cleanInput("011188360"));
    }

    @Test
    public void testCleanInput_SSN_USA_start_space() {
        assertEquals("011188360", SSN_USA.cleanInput(" 011188360"));
    }

    @Test
    public void testCleanInput_SSN_USA_end_space() {
        assertEquals("011188360", SSN_USA.cleanInput("011188360 "));
    }

    @Test
    public void testCleanInput_SSN_USA() {
        assertEquals("011188360", SSN_USA.cleanInput("011-18-8360"));
    }

    // SIN_CA

    @Test
    public void testFromName_SIN_CA() {
        NationalIdType type = NationalIdType.getNationalIdTypeFromName("SIN");
        assertEquals(SIN_CA, type);
    }

    @Test
    public void testIsValid_SIN_CA_null() {
        assertFalse(SIN_CA.isValid(null));
    }

    @Test
    public void testIsValid_SIN_CA_empty() {
        assertFalse(SIN_CA.isValid(""));
    }

    @Test
    public void testIsValid_SIN_CA_blank() {
        assertFalse(SIN_CA.isValid("    "));
    }

    @Test
    public void testIsValid_SIN_CA_text() {
        assertFalse(SIN_CA.isValid("wibble"));
    }

    @Test
    public void testIsValid_SIN_CA_valid() {
        assertTrue(SIN_CA.isValid("241678895"));
    }

    @Test
    public void testCleanInput_SIN_CA_already_clean() {
        assertEquals("241678895", SIN_CA.cleanInput("241678895"));
    }

    @Test
    public void testCleanInput_SIN_CA_start_space() {
        assertEquals("241678895", SIN_CA.cleanInput(" 241678895"));
    }

    @Test
    public void testCleanInput_SIN_CAA_end_space() {
        assertEquals("241678895", SIN_CA.cleanInput("241678895 "));
    }

    // HKID_HK

    @Test
    public void testFromName_HKID_HK() {
        NationalIdType type = NationalIdType.getNationalIdTypeFromName("HKID");
        assertEquals(HKID_HK, type);
    }

    @Test
    public void testIsValid_HKID_HK_null() {
        assertFalse(HKID_HK.isValid(null));
    }

    @Test
    public void testIsValid_HKID_HK_empty() {
        assertFalse(HKID_HK.isValid(""));
    }

    @Test
    public void testIsValid_HKID_HK_blank() {
        assertFalse(HKID_HK.isValid("    "));
    }

    @Test
    public void testIsValid_HKID_HK_text() {
        assertFalse(HKID_HK.isValid("wibble"));
    }

    @Test
    public void testIsValid_HKID_HK_valid() {
        // TODO: What's a valid ID?
    }

    @Test
    public void testCleanInput_HKID_HK_already_clean() {
        // TODO: What's a valid ID?
    }

    @Test
    public void testCleanInput_HKID_HK_start_space() {
        // TODO: What's a valid ID?
    }

    @Test
    public void testCleanInput_HKID_HK_end_space() {
        // TODO: What's a valid ID?
    }

    // BSN_NL

    @Test
    public void testFromName_BSN_NL() {
        NationalIdType type = NationalIdType.getNationalIdTypeFromName("BSN");
        assertEquals(BSN_NL, type);
    }

    @Test
    public void testIsValid_BSN_NL_null() {
        assertFalse(BSN_NL.isValid(null));
    }

    @Test
    public void testIsValid_BSN_NL_empty() {
        assertFalse(BSN_NL.isValid(""));
    }

    @Test
    public void testIsValid_BSN_NL_blank() {
        assertFalse(BSN_NL.isValid("    "));
    }

    @Test
    public void testIsValid_BSN_NL_text() {
        assertFalse(BSN_NL.isValid("wibble"));
    }

    @Test
    public void testIsValid_BSN_NL_valid() {
        assertTrue(BSN_NL.isValid("915062999"));
    }

    @Test
    public void testCleanInput_BSN_NL_already_clean() {
        assertEquals("915062999", BSN_NL.cleanInput("915062999"));
    }

    @Test
    public void testCleanInput_BSN_NL_start_space() {
        assertEquals("915062999", BSN_NL.cleanInput(" 915062999"));
    }

    @Test
    public void testCleanInput_BSN_NL_end_space() {
        assertEquals("915062999", BSN_NL.cleanInput("915062999 "));
    }

    @Test
    public void testCleanInput_BSN_NL_whitespace() {
        assertEquals("915062999", BSN_NL.cleanInput("915 062 999"));
    }

    // KNV_DE

    @Test
    public void testFromName_KVN_DE() {
        NationalIdType type = NationalIdType.getNationalIdTypeFromName("Krankenversichertennummer");
        assertEquals(KVN_DE, type);
    }

    @Test
    public void testIsValid_KVN_DE() {
        // German Krankenversichertennummer
        // a few valid numbers: G344907624, E624149366, A567427800, Z683143698  -- these are random digits with calculated check-digit to match
        NationalIdType typeDe = NationalIdType.getNationalIdTypeFromName("Krankenversichertennummer");
        assertNotNull(typeDe);
        assertTrue(typeDe.isValid("G344907624")); // true
        assertTrue(typeDe.isValid("E624149366")); // true
        assertTrue(typeDe.isValid("A567427800")); // true
        assertTrue(typeDe.isValid("Z683143698")); // true
        //		clean input that's already valid
        assertTrue(typeDe.isValid(typeDe.cleanInput("G344907624")));
        //		clean input uppercase and added spaces
        assertTrue(typeDe.isValid(typeDe.cleanInput(" g  344 907 624 ")));
        //		various checks WITHOUT using cleanInput.  Only a fully-correct code should pass
        //		Length is less then 10
        //		Length is greater then 10
        //		First character is space
        //		First character is number
        //		First character is Unicode
        //		Second character is letter
        assertFalse(typeDe.isValid("Z68314369"));
        assertFalse(typeDe.isValid("Z6831436980")); // valid but with extra digit
        assertFalse(typeDe.isValid(" 6831436980"));
        assertFalse(typeDe.isValid("26831436980"));
        assertFalse(typeDe.isValid("€6831436980"));
        assertFalse(typeDe.isValid("ZG831436980"));

        // first char lowercase - without cleanInput()
        assertFalse(typeDe.isValid("a567427800"));
        //		Last character doesn't match checking algorithm
        //		Example: A000000000
        //		Example: A000000002
        //		Example: Z999999997 2 62 9 92 9 92 9 92 9 9*2 2+3+9+9+9+9+9+9+9+9=77
        assertFalse(typeDe.isValid("A000000001"));
        assertTrue(typeDe.isValid("A000000002"));
        assertFalse(typeDe.isValid("A000000003"));

        assertTrue(typeDe.isValid("Z999999997"));
    }

    // CIVIL_ID_KW

    @Test
    public void testFromName_CIVIL_ID_KW() {
        NationalIdType type = NationalIdType.getNationalIdTypeFromName("Civil ID");
        assertEquals(CIVIL_ID_KW, type);
    }

    @Test
    public void testIsValid_CIVIL_ID_KW_null() {
        assertFalse(CIVIL_ID_KW.isValid(null));
    }

    @Test
    public void testIsValid_CIVIL_ID_KW_empty() {
        assertFalse(CIVIL_ID_KW.isValid(""));
    }

    @Test
    public void testIsValid_CIVIL_ID_KW_blank() {
        assertFalse(CIVIL_ID_KW.isValid("    "));
    }

    @Test
    public void testIsValid_CIVIL_ID_KW_text() {
        assertFalse(CIVIL_ID_KW.isValid("wibble"));
    }

    @Test
    public void testIsValid_CIVIL_ID_KW_valid() {
        assertTrue(CIVIL_ID_KW.isValid("214052167877"));
    }

    @Test
    public void testCleanInput_CIVIL_ID_KW_already_clean() {
        assertEquals("214052167877", CIVIL_ID_KW.cleanInput("214052167877"));
    }

    @Test
    public void testCleanInput_CIVIL_ID_KW_start_space() {
        assertEquals("214052167877", CIVIL_ID_KW.cleanInput(" 214052167877"));
    }

    @Test
    public void testCleanInput_CIVIL_ID_KW_end_space() {
        assertEquals("214052167877", CIVIL_ID_KW.cleanInput("214052167877 "));
    }

    @Test
    public void testGetNationalIdTypeFromFhirIdentifierSystem_shouldReturnCorrectNationalIdForNhs() {
        assertEquals(NHS_NUMBER, NationalIdType.getNationalIdTypeFromFhirIdentifierSystem("https://fhir.nhs.uk/Id/nhs-number"));
    }

    @DataProvider
    public static Object[][] nationalIdTypeToCountryCodeMappings() {
        return new Object[][] {
                { "GB-ENG", ImmutableSet.of(NHS_NUMBER) },
                { "GB-WLS", ImmutableSet.of(NHS_NUMBER) },
                { "GB-SCT", ImmutableSet.of(CHI_NUMBER) },
                { "GB-NIR", ImmutableSet.of(H_AND_C_NUMBER) },
                { "IE", ImmutableSet.of(IHI_NUMBER) },
                { "US", ImmutableSet.of(SSN_USA) },
                { "CA", ImmutableSet.of(SIN_CA) },
                { "HK", ImmutableSet.of(HKID_HK) },
                { "NL", ImmutableSet.of(BSN_NL) },
                { "DE", ImmutableSet.of(KVN_DE) },
                { "KW", ImmutableSet.of(CIVIL_ID_KW) }
        };
    }

    @Test
    @UseDataProvider("nationalIdTypeToCountryCodeMappings")
    public void oneCountryCodeShouldMapStrictlyToOneNationalId(String countryCode, Set<NationalIdType> nationalIdTypes) {
        // GIVEN - test input in arguments

        // WHEN
        Set<NationalIdType> actual = Stream.of(NationalIdType.values())
                .filter(idType -> idType.getCountryCodes().contains(countryCode))
                .collect(toSet());

        // THEN
        assertThat(actual, sameBeanAs(nationalIdTypes));
    }

}
