package com.pkb.entities.enums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * This enum describes the National ID types that PKB supports; each enum element includes
 * name, iso country code, and an isValid(id) implementation.
 *
 * @author robwhelan
 */
public enum NationalIdType implements Serializable {

    /**
     * Source: http://oid-info.com/get/2.16.840.1.113883.2.1.4.1
     */
    NHS_NUMBER("NHS number", new String[] { "GB-ENG", "GB-WLS" }, "NHS", "NH", "https://fhir.nhs.uk/Id/nhs-number") {
        @Override
        public String cleanInput(String id) {
            // strip out dashes and spaces
            return StringUtils.trimToNull(StringUtils.replaceChars(id, "- ", ""));
        }

        @Override
        public boolean isValid(String cleanId) {
            // see http://www.datadictionary.nhs.uk/data_dictionary/attributes/n/nhs_number_de.asp
            // NOTE: we're allowing the test ranges (for test patients) -- in future we could disallow in some cases

            // illegal range:
            //   001 000 0010 to 319 999 9999 is the Scottish CHI
            //   320 000 001 to 399 999 999 is the NI H&C number range
            // test ranges:
            //   500 000 000 - 599 999 999
            //   900 000 000 - 999 999 999

            if (!StringUtils.isBlank(cleanId) && StringUtils.isNumeric(cleanId) && (cleanId.length() == 10)) {
                // make int array
                int[] ints = new int[cleanId.length()];
                for (int i = 0; i < cleanId.length(); i++) {
                    ints[i] = Character.digit(cleanId.charAt(i), 10);
                }
                // illegal range: first char 3 or under
                if (ints[0] <= 3) {
                    return false;
                }

                // remove test ranges? block if 1st char is 5 or 9

                // modulus 11 check
                int mod11 = (ints[0] * 10) + (ints[1] * 9) + (ints[2] * 8) + (ints[3] * 7) + (ints[4] * 6) + (ints[5] * 5) + (ints[6] * 4)
                        + (ints[7] * 3) + (ints[8] * 2);
                mod11 = 11 - (mod11 % 11);
                if ((mod11 == ints[9])
                        || ((mod11 == 11) && (ints[9] == 0))) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String getCanonicalFormat(String id) {
            // 012-345-6789
            String clean = cleanInput(id);
            if (StringUtils.isEmpty(clean) || (clean.length() != 10)) {
                return id;
            }
            // official standard display is separated by spaces; we're ignoring this to make copy/paste easier
            // return clean.substring(0,3) +" "+ clean.substring(3,6) +" "+ clean.substring(6);
            return clean;
        }
    },

    /**
     * Source: http://oid-info.com/get/2.16.840.1.113883.2.1.3.2 -> http://www.hl7.org.uk/version3group/downloads/OidRootHl7UkOnly.html
     */
    CHI_NUMBER("CHI number", "GB-SCT", "NHS Scotland", "NH", "urn:oid:2.16.840.1.113883.2.1.3.2.4.16.53") {
        @Override
        public String cleanInput(String id) {
            // strip out dashes and spaces
            return StringUtils.trimToNull(StringUtils.replaceChars(id, "- ", ""));
        }

        @Override
        public boolean isValid(String cleanId) {
            // CHI number: http://www.datadictionaryadmin.scot.nhs.uk/SMR-Datasets/SMR-Validation-Section/Patient-Identification-Section/Community-Health-Index-Number.asp
            // Source: document rec'd from David Reardon, NHSGGC Health Information & Technology
            // Validation:
            // - first 6 digits are patient's date of birth in DDMMYY format (so first 2 digits must be 01-31)
            // - Digits 1-6 are the patients DOB in DDMMYY format
            //      (so first 2 must be 01-31, and next 2 must be 01-12)
            // - Digits 7-8 are random numbers
            // - Digit 9 is a random number that will be odd for a male and even for a female.
            // - Digit 10 is a check digit.
            // - ERK: here: http://www.datadictionary.wales.nhs.uk/WordDocuments/nhsnumber.htm it says (of the NHS check digit
            //		algo) "This algorithm applies to the Welsh and English NHS Number and the Northern Ireland Health & Care
            //		Number. The check digit algorithm for the Scottish CHI Number is available on request from the NHS Wales
            //		Informatics Service."
            //   That's not accurate -- the mod 11 calc is exactly the same as the NHS number calc.

            if (!StringUtils.isBlank(cleanId) && StringUtils.isNumeric(cleanId) && (cleanId.length() == 10)) {
                String dayChars = cleanId.substring(0, 2);
                if (dayChars.startsWith("0")) {
                    dayChars = dayChars.substring(1);
                }
                int dayInt = Integer.parseInt(dayChars);
                if ((dayInt < 1) || (dayInt > 31)) {
                    return false;
                }

                String monthChars = cleanId.substring(2, 4);
                if (monthChars.startsWith("0")) {
                    monthChars = monthChars.substring(1);
                }
                int monthInt = Integer.parseInt(monthChars);
                if ((monthInt < 1) || (monthInt > 12)) {
                    return false;
                }

                // make int array
                int[] ints = new int[cleanId.length()];
                for (int i = 0; i < cleanId.length(); i++) {
                    ints[i] = Character.digit(cleanId.charAt(i), 10);
                }
                // modulus 11 check
                int mod11 = (ints[0] * 10) + (ints[1] * 9) + (ints[2] * 8) + (ints[3] * 7) + (ints[4] * 6) + (ints[5] * 5) + (ints[6] * 4)
                        + (ints[7] * 3) + (ints[8] * 2);
                mod11 = 11 - (mod11 % 11);
                if ((mod11 == ints[9])
                        || ((mod11 == 11) && (ints[9] == 0))) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String getCanonicalFormat(String id) {
            // 012 345 6789
            String clean = cleanInput(id);
            if (StringUtils.isEmpty(clean) || (clean.length() != 10)) {
                return id;
            }
            // official standard display is separated by spaces; we're ignoring this to make copy/paste easier
            // return clean.substring(0,3) +" "+ clean.substring(3,6) +" "+ clean.substring(6);
            return clean;
        }
    },

    /**
     * Source: http://oid-info.com/get/2.16.840.1.113883.3.1061 (http://www.hl7.org/oid/index.cfm?Comp_OID=2.16.840.1.113883.3.1061)
     */
    H_AND_C_NUMBER("Health and Care number", "GB-NIR", "DHSSPS", "NH") {
        @Override
        public String cleanInput(String id) {
            // strip out dashes and spaces
            return StringUtils.trimToNull(StringUtils.replaceChars(id, "- ", ""));
        }

        @Override
        public boolean isValid(String cleanId) {
            // validation is like NHS number (random plus the same check digit algorithm)
            // BUT: The first two characters of the Health and Care Number must always lie within the range 32 - 39
            // http://www.datadictionary.wales.nhs.uk/WordDocuments/healthandcarenumber.htm

            if (!StringUtils.isBlank(cleanId) && StringUtils.isNumeric(cleanId) && (cleanId.length() == 10)) {
                String first2Char = cleanId.substring(0, 2);
                if (first2Char.startsWith("0")) {
                    first2Char = first2Char.substring(1);
                }
                int first2Int = Integer.parseInt(first2Char);
                if ((first2Int < 32) || (first2Int > 39)) {
                    return false;
                }

                // make int array
                int[] ints = new int[cleanId.length()];
                for (int i = 0; i < cleanId.length(); i++) {
                    ints[i] = Character.digit(cleanId.charAt(i), 10);
                }
                // modulus 11 check
                int mod11 = (ints[0] * 10) + (ints[1] * 9) + (ints[2] * 8) + (ints[3] * 7) + (ints[4] * 6) + (ints[5] * 5) + (ints[6] * 4)
                        + (ints[7] * 3) + (ints[8] * 2);
                mod11 = 11 - (mod11 % 11);
                if ((mod11 == ints[9])
                        || ((mod11 == 11) && (ints[9] == 0))) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String getCanonicalFormat(String id) {
            // 012 345 6789
            String clean = cleanInput(id);
            if (StringUtils.isEmpty(clean) || (clean.length() != 10)) {
                return id;
            }
            // official standard display is separated by spaces; we're ignoring this to make copy/paste easier
            // return clean.substring(0,3) +" "+ clean.substring(3,6) +" "+ clean.substring(6);
            return clean;
        }
    },

    IHI_NUMBER("IHI number", "IE", "DSP", "NI") {
        @Override
        public String cleanInput(String id) {
            return StringUtils.trimToNull(StringUtils.replaceChars(StringUtils.upperCase(id), "- ", ""));
        }

        @Override
        public boolean isValid(String cleanId) {
            return cleanId != null
                    && cleanId.length() == 10
                    && cleanId.codePoints().allMatch(Character::isDigit);
                    // Check digit, TBC: && cleanId.codePoints().map(cp -> Integer.parseInt(Character.toString((char) cp))).sum() % 11 == ?;
        }

        @Override
        public String getCanonicalFormat(String id) {
            return cleanInput(id);
        }
    },

    /**
     * Source: https://www.hl7.org/fhir/identifier-registry.html (http://hl7.org/fhir/sid/us-ssn)
     */
    SSN_USA("SSN", "US", "USA", "NI", "http://hl7.org/fhir/sid/us-ssn") {
        @Override
        public String cleanInput(String id) {
            // strip out dashes
            return StringUtils.trimToNull(StringUtils.replaceChars(id, "-", ""));
        }

        @Override
        public boolean isValid(String cleanId) {
            // see http://en.wikipedia.org/wiki/Social_Security_number#Valid_SSNs
            // and http://ssa-custhelp.ssa.gov/app/answers/detail/a_id/425
            if (!StringUtils.isBlank(cleanId) && StringUtils.isNumeric(cleanId) && (cleanId.length() == 9)) {
                // Invalid ranges and groupings:

                // Numbers with all zeros in any digit group (000-##-####, ###-00-####, ###-##-0000)
                if (cleanId.startsWith("000") || cleanId.substring(3, 5).equals("00") || cleanId.endsWith("0000")) {
                    return false;
                }
                // Numbers of the form 666-##-####
                if (cleanId.startsWith("666")) {
                    return false;
                }
                // Numbers in the 900 series
                if (cleanId.startsWith("9")) {
                    return false;
                }
                // Hilda's number 078-05-1120
                if (cleanId.equals("078051120")) {
                    return false;
                }

                // passed tests!
                return true;
            }
            return false;
        }

        @Override
        public String getCanonicalFormat(String id) {
            // 012-34-5678
            String clean = cleanInput(id);
            if (StringUtils.isEmpty(clean) || (clean.length() != 9)) {
                return id;
            }
            return clean.substring(0, 3) + "-" + clean.substring(3, 5) + "-" + clean.substring(5);
        }
    },

    /**
     * Source: http://oid-info.com/get/2.16.840.1.113883.4.272 (http://www.hl7.org/oid/index.cfm?Comp_OID=2.16.840.1.113883.4.272)
     */
    SIN_CA("SIN", "CA", "CA", "NI") {
        @Override
        public String cleanInput(String id) {
            // strip out spaces
            return StringUtils.trimToNull(StringUtils.replaceChars(id, " ", ""));
        }

        @Override
        public boolean isValid(String cleanId) {
            // see http://en.wikipedia.org/wiki/Social_Insurance_Number#Validation
            // NOTE: currently accepting all initial digits, though in reality 8 and 0 aren't used (so are invalid)
            if (!StringUtils.isBlank(cleanId) && StringUtils.isNumeric(cleanId) && (cleanId.length() == 9)) {
                // make int array
                int[] multipliers = new int[] { 1, 2, 1, 2, 1, 2, 1, 2, 1 };
                int luhnRootSum = 0;
                for (int i = 0; i < 9; i++) {
                    int product = multipliers[i] * Character.digit(cleanId.charAt(i), 10);
                    if (product < 10) {
                        luhnRootSum += product;
                    } else {
                        luhnRootSum += (product - 10) + 1; // add the digits.. but cheat: max product is 18
                    }
                }

                if ((luhnRootSum % 10) == 0) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String getCanonicalFormat(String id) {
            String clean = cleanInput(id);
            if (StringUtils.isEmpty(clean) || (clean.length() != 9)) {
                return id;
            }
            return clean.substring(0, 3) + " " + clean.substring(3, 6) + " " + clean.substring(6);
        }
    },

    // TODO: find URI for identifier system
    HKID_HK("HKID", "HK", "HK", "NI") {
        @Override
        public String cleanInput(String id) {
            // strip out parens
            return StringUtils.trimToNull(StringUtils.replaceChars(id, "()", ""));
        }

        @Override
        public boolean isValid(String cleanId) {
            // see http://en.wikipedia.org/wiki/Hong_Kong_Identity_Card
            // and http://www.xml.gov.hk/en/approved/hkid_number_v1_0.htm
            // check scheme: http://quintus.roothome.net/Projects/_test/HKID.phps
            // another check scheme impl: http://www.biswaretech.com/blog22.aspx
            // they're all different! but this claims to source wikipedia
            //   http://kopanda.creativeworks.com.hk/2010/03/08/hkid-card-check-digit-validation-with-cakephp/
            // 1 or 2 alpha chars, then 6 digits, then check digit (0-9 or A) in parens
            // luhn check variant:
            // convert the first 1 or 2 alpha chars to digits like this:
            // - if first is missing, treat like space (char=32)
            // - otherwise: (ord(strtoupper($ch))-ord('A')+10);  -- huh. There's lots of disagreement on this!
            if (StringUtils.isBlank(cleanId)) {
                return false; // catch out null or empty before starting...
            }

            // uppercase and strip parens  (No -- they need to get it right at entry, for now, since that's going into the DB...)
            // id = StringUtils.replaceChars(id.toUpperCase(), "()", "");

            int[] ints = new int[8];

            if (cleanId.length() == 8) {
                cleanId = " " + cleanId; // make it 9 chars long...
            }
            if (cleanId.length() != 9) {
                return false;
            }

            if (!StringUtils.isNumeric(cleanId.substring(2, 8))) {
                return false;
            }

            char checkChar = cleanId.charAt(8); // last char
            ints[0] = charToInt(cleanId.charAt(0));
            ints[1] = charToInt(cleanId.charAt(1));
            for (int i = 2; i < 8; i++) {
                ints[i] = Character.digit(cleanId.charAt(i), 10);
            }

            // Okay!  We have a check character, and 8 ints for the mod 11 check.
            int mod11 = (ints[0] * 9) + (ints[1] * 8) + (ints[2] * 7) + (ints[3] * 6) + (ints[4] * 5) + (ints[5] * 4) + (ints[6] * 3)
                    + (ints[7] * 2);
            mod11 = 11 - (mod11 % 11);
            if (("" + mod11).equals(Character.toString(checkChar))
                    || ((mod11 == 11) && (checkChar == '0'))
                    || ((mod11 == 10) && (checkChar == 'A'))) {
                return true;
            }
            return false;
        }

        private int charToInt(char c) {
            // http://kopanda.creativeworks.com.hk/2010/03/08/hkid-card-check-digit-validation-with-cakephp/
            //        	if( c == ' ' )
            //        		return 0;
            //        	else
            //        		return (c - 'A')%11 + 1;

            // https://groups.google.com/forum/?hl=en&fromgroups=#!topic/hk.comp.pc/6FUkKtj_2YI
            if (c == ' ') {
                return 91;
            } else {
                return c;
            }

            // http://keatonchan.blogspot.fr/2009/08/hkid-check-digit-calculation-algorithm.html
            // c-'A' + 10, ' ' is 36
            // valid IDs for testing: B123456(6), P135143(9), A182361(5), CA182361(1), AB123456(9), ZA182361(3), AZ182361(6), XZ182361(8), ZX182361(6), XX182361(2), CA182367(0), CA182362(A)
        }

        @Override
        public String getCanonicalFormat(String id) {
            String clean = cleanInput(id);
            if (StringUtils.isEmpty(clean) || (clean.length() < 8)) {
                return id;
            }
            int lastCharId = clean.length() - 1;
            return clean.substring(0, lastCharId) + "(" + clean.substring(lastCharId) + ")";
        }
    },

    /**
     * Source: https://simplifier.net/nictizdstu2/unnamednamingsystem8
     */
    BSN_NL("BSN", "NL", "NL", "NI", "http://fhir.nl/fhir/NamingSystem/bsn") {
        @Override
        public String cleanInput(String id) {
            // strip out spaces
            return StringUtils.trimToNull(StringUtils.replaceChars(id, " ", ""));
        }

        @Override
        public boolean isValid(String cleanId) {
            // see http://nl.wikipedia.org/wiki/Burgerservicenummer  (translation widget at top of page)
            // NOTE: we're allowing the test range (900000000 to 999999999) but could disallow in some cases
            /*
             * if number is represented by ABCDEFGHI, then:
             * (9 x A) + (8 x B) + (C x 7) + (6 x D) + (5 x E) + (F x 4) + (G x 3) + (2 x H) + (- 1 x I) is a multiple of 11.
             * Valid examples are: 111222333 and 123 456 782.
             *
             * An 8-digit BSN (a relic from the time when the social security number was used) is used for monitoring should be provided with a leading zero.
             *
             * An easy way to generate test numbers: choose 4 random numbers (eg 9413)
             *   add the reverse of the selected number (reversed: 3149, thus: 94,133,149),
             *   and finally add a 0.  Result: 941,331,490.
             */
            if (!StringUtils.isBlank(cleanId) && (cleanId.length() == 8)) {
                // pad with a 0 for checking, if it's only 8 digits
                cleanId = "0" + cleanId;
            }
            // now check it
            if (!StringUtils.isBlank(cleanId) && StringUtils.isNumeric(cleanId) && (cleanId.length() == 9)) {
                // make int array
                int[] ints = new int[cleanId.length()];
                for (int i = 0; i < cleanId.length(); i++) {
                    ints[i] = Character.digit(cleanId.charAt(i), 10);
                }
                // modified modulus 11 check
                int mod11 = (ints[0] * 9) + (ints[1] * 8) + (ints[2] * 7) + (ints[3] * 6) + (ints[4] * 5) + (ints[5] * 4) + (ints[6] * 3)
                        + (ints[7] * 2) + (ints[8] * (-1));
                mod11 = (mod11 % 11);
                if (mod11 == 0) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String getCanonicalFormat(String id) {
            String clean = cleanInput(id);
            if (StringUtils.isEmpty(clean) || (clean.length() != 9)) {
                return id;
            }
            return clean.substring(0, 3) + " " + clean.substring(3, 6) + " " + clean.substring(6);
        }
    },

    /**
     * Source: Maybe: http://oid-info.com/get/2.16.840.1.113883.2.6 or http://fhir.de/NamingSystem/gkv/kvnr-30
     */
    KVN_DE("Krankenversichertennummer", "DE", "DE", "NH", "http://fhir.de/NamingSystem/gkv/kvnr-30") {
        @Override
        public String cleanInput(String id) {
            return Optional.ofNullable(id)
                    .map(String::toUpperCase)
                    .map(uppercaseId -> StringUtils.replaceChars(uppercaseId, " ", ""))
                    .map(StringUtils::trimToNull)
                    .orElse(null);
            // strip out spaces and make sure alpha char is uppercase
        }

        @Override
        public boolean isValid(String cleanId) {
            // see JIRA PHR-857 and PDF: ftp://ftp.kbv.de/ita-update/Abrechnung/KBV_ITA_VGEX_Datensatzbeschreibung_KVDT.pdf
            // starts with alpha, then 9 numeric digits (last is the check digit)
            // convert alpha to 2-digit number (e.g., A=01, B=02, etc.
            // multiply each second digit by 2, sum any multiple digits, then sum all those results and %10: result = check digit
            if (!StringUtils.isBlank(cleanId) && (cleanId.length() == 10) && StringUtils.isAlpha(cleanId.substring(0, 1))
                    && StringUtils.isNumeric(cleanId.substring(1, 10))) {
                // convert alpha letter into 2-digit string
                int alphaSequence = (cleanId.charAt(0) - 'A') + 1; // 1-based counting
                String alphaDigits = (alphaSequence < 10) ? "0" + alphaSequence : "" + alphaSequence;
                int checkDigit = Character.digit(cleanId.charAt(9), 10);
                String luhnInput = alphaDigits + cleanId.substring(1, 9); // skipping prefix alpha and suffix check digit

                // make int array
                int[] multipliers = new int[] { 1, 2, 1, 2, 1, 2, 1, 2, 1, 2 };
                int luhnRootSum = 0;
                for (int i = 0; i < 10; i++) {
                    int product = multipliers[i] * Character.digit(luhnInput.charAt(i), 10);
                    if (product < 10) {
                        luhnRootSum += product;
                    } else {
                        luhnRootSum += (product - 10) + 1; // add the digits.. but cheat: max product is 18
                    }
                }

                if ((luhnRootSum % 10) == checkDigit) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String getCanonicalFormat(String id) {
            return cleanInput(id); // no standard formatting that I know of!  Just the clean input, no spaces.
        }
    },

    // TODO: find URI for identifier system
    CIVIL_ID_KW("Civil ID", "KW", "KW", "NI") {
        // http://dr-mahbob.com/blog/2008/12/23/civil-id-equation-for-check-digit/
        // Sample code from Django: https://github.com/django/django-localflavor/blob/master/localflavor/kw/forms.py
        // sample numbers:
        // 273032401586  -- this seems to be a common sample number
        // 288090100173
        // 233122100058
        // 286090401364   DoB: September 4 1986
        @Override
        public String cleanInput(String id) {
            // strip out dashes or spaces
            return StringUtils.trimToNull(StringUtils.replaceChars(id, " -", ""));
        }

        @Override
        public boolean isValid(String cleanId) {
            //   http://dr-mahbob.com/blog/2008/12/23/civil-id-equation-for-check-digit/
            //   Sample code from Django: https://github.com/django/django-localflavor/blob/master/localflavor/kw/forms.py
            // Validation:
            // - 12 digits
            // - Digit 1 ...may... be a century digit -- "2" for pre-2000 DoB years, and "3" for year 2000+ DoB.  Not confirmed, so not yet validated.
            // - Digits 2-7 are the patients DOB in YYMMDD format
            //      (so the first 2 can be anything, next 2 must be 01-12 and last 2 must be 01-31)
            // - Digits 8-11 are a sequence number (anything)
            // - Digit 12 is a check digit.
            // - Calc: 11 - Mod(((C1 * 2) + (C2 * 1)  + (C3 * 6) + (C4 * 3) + (C5 * 7) +
            //                   (C6 * 9) + (C7 * 10) + (C8 * 5) + (C9 * 8) + (C10 * 4) + (C11 * 2)),11) = C12

            if (!StringUtils.isBlank(cleanId) && StringUtils.isNumeric(cleanId) && (cleanId.length() == 12)) {
                String monthChars = cleanId.substring(3, 5);
                if (monthChars.startsWith("0")) {
                    monthChars = monthChars.substring(1);
                }
                int monthInt = Integer.parseInt(monthChars);
                if ((monthInt < 1) || (monthInt > 12)) {
                    return false;
                }

                String dayChars = cleanId.substring(5, 7);
                if (dayChars.startsWith("0")) {
                    dayChars = dayChars.substring(1);
                }
                int dayInt = Integer.parseInt(dayChars);
                if ((dayInt < 1) || (dayInt > 31)) {
                    return false;
                }

                // make int array
                int[] ints = new int[cleanId.length()];
                for (int i = 0; i < cleanId.length(); i++) {
                    ints[i] = Character.digit(cleanId.charAt(i), 10);
                }
                // modulus 11 check (with randomly-chosen multipliers...?)
                // (C1 * 2) + (C2 * 1)  + (C3 * 6) + (C4 * 3) + (C5 * 7) + (C6 * 9) + (C7 * 10) + (C8 * 5) + (C9 * 8) + (C10 * 4) + (C11 * 2)
                int mod11 = (ints[0] * 2) + (ints[1] * 1) + (ints[2] * 6) + (ints[3] * 3) + (ints[4] * 7) + (ints[5] * 9) + (ints[6] * 10)
                        + (ints[7] * 5) + (ints[8] * 8) + (ints[9] * 4) + (ints[10] * 2);
                mod11 = 11 - (mod11 % 11);
                if ((mod11 == ints[11])
                        || ((mod11 == 11) && (ints[11] == 0))) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String getCanonicalFormat(String id) {
            return cleanInput(id);
        }
    };

    private String name;

    /**
     * This is a list of ISO country codes for which the corresponding
     * NationalIdType applies.
     * A list is needed because there is not quite a 1-to-1 between
     * NationalIdType and "country". For example, NHS_NUMBER is used by both
     * England and Wales.
     */
    private List<String> countryCodes;

    private String hl7IdAuthority;

    /**
     * "NH" for National Health Plan ID, "NI" for National Unique Individual ID... see HL7 Spy
     */
    private String hl7IdType;

    private String fhirIdentifierSystem;

    public abstract String cleanInput(String id);

    /**
     * Determines if a value is valid for this {@code NationalIdType}. Note: it is the caller's responsibility to have first called
     * cleanInput() on the value to be checked.
     *
     * @param cleanId
     *            The result of calling cleanInput() on the value to be checked.
     * @return true - if and only if the value is valid for this type. false - otherwise.
     */
    public abstract boolean isValid(String cleanId);

    public abstract String getCanonicalFormat(String id);

    NationalIdType(String name, String countryCode, String hl7IdAuthority, String hl7IdType) {
        this(name, countryCode, hl7IdAuthority, hl7IdType, null);
    }

    NationalIdType(String name, String countryCode, String hl7IdAuthority, String hl7IdType, String fhirIdentifierSystem) {
        this(name, new String[] { countryCode }, hl7IdAuthority, hl7IdType, fhirIdentifierSystem);
    }

    NationalIdType(String name, String[] countryCodeArray, String hl7IdAuthority, String hl7IdType) {
        this(name, countryCodeArray, hl7IdAuthority, hl7IdType, null);
    }

    NationalIdType(String name, String[] countryCodeArray, String hl7IdAuthority, String hl7IdType, String fhirIdentifierSystem) {
        List<String> countryCodes = new ArrayList<>();
        countryCodes.addAll(Arrays.asList(countryCodeArray));
        this.name = name;
        this.countryCodes = countryCodes;
        this.hl7IdAuthority = hl7IdAuthority;
        this.hl7IdType = hl7IdType;
        this.fhirIdentifierSystem = fhirIdentifierSystem;
    }

    public static NationalIdType getNationalIdTypeFromCountryCode(String countryCode) {
        for (NationalIdType nit : values()) {
            if (nit.getCountryCodes().contains(countryCode)) {
                return nit;
            }
        }
        return null;
    }

    public static NationalIdType getNationalIdTypeFromName(String name) {
        for (NationalIdType nit : values()) {
            if (nit.getName().equals(name)) {
                return nit;
            }
        }
        return null;
    }

    public static NationalIdType getNationalIdTypeFromAuthorityAndType(String authority, String type) {
        for (NationalIdType nit : values()) {
            if (nit.getHl7IdAuthority().equals(authority) &&
                    nit.getHl7IdType().equals(type)) {
                return nit;
            }
        }
        return null;
    }

    public static NationalIdType getNationalIdTypeFromFhirIdentifierSystem(@NotNull String fhirIdentifierSystem) {
        for (NationalIdType nit : values()) {
            if (fhirIdentifierSystem.equals(nit.fhirIdentifierSystem)) {
                return nit;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public List<String> getCountryCodes() {
        return countryCodes;
    }

    public String getHl7IdAuthority() {
        return hl7IdAuthority;
    }

    public String getHl7IdType() {
        return hl7IdType;
    }

    public String getFhirIdentifierSystem() {
        return fhirIdentifierSystem;
    }
}
