package com.pkb.entities.enums;

import java.lang.invoke.MethodHandles;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The available genders are taken from the NHS data dictionary.
 * See: http://www.datadictionary.nhs.uk/data_dictionary/attributes/p/person/person_phenotypic_sex_classification_de.asp
 */
public enum Gender {

    UNKNOWN(-1, "unknown"),
    MALE(1, "male"),
    FEMALE(0, "female"),
    INDETERMINATE(9, "indeterminate");

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // TODO: Do we actually need either of these two fields? See PHR-2890.
    private final int id;
    private final String code;

    Gender(int id, String code) {
        this.id = id;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public static Gender getByCode(String code) {
        for (Gender candidate : values()) {
            if (candidate.code.equals(code)) {
                return candidate;
            }
        }
        // This used to return null, which looks wrong. Not changing to throw exception.
        LOGGER.error("Invalid gender code. Expected unknown, male, female or indeterminate. Received {}", code);
        return UNKNOWN;
    }

    public static String toLetter(int id) {
        return getById(id).name().toUpperCase().substring(0, 1);
    }

    public static Gender getById(int id) {
        for (Gender g : values()) {
            if (g.id == id) {
                return g;
            }
        }
        throw new IllegalArgumentException("Invalid gender. Expected -1, 1, 0 or 9. Received " + id);
    }

    public static Gender getByHL7(@NotNull String hl7WordCode) {
        // In order to provide backwards compatibility, we still support AMBIGUOUS for the HL7 API. See PHR-2851, PHR-2889.
        if ("A".equals(hl7WordCode.trim().toUpperCase().substring(0, 1))) {
            LOGGER.warn("PHR-2851: HL7 API received a gender code of A");
            return Gender.UNKNOWN;
        }
        return getByWord(hl7WordCode);
    }

    public static Gender getByWord(@NotNull String wordCode) {
        // We support words that _start with_ something we expect for HL7 and EMIS.
        // TODO: Are we being too helpful for our own good?
        return getByLetter(wordCode.trim().toUpperCase().substring(0, 1));
    }

    public static Gender getByLetter(@NotNull String letterCode) {
        switch (letterCode) {
            case "U":
                return Gender.UNKNOWN;
            case "M":
                return Gender.MALE;
            case "F":
                return Gender.FEMALE;
            case "I":
                return Gender.INDETERMINATE;
        }
        throw new IllegalArgumentException("Invalid gender. Expected U, M, F or I. Received " + letterCode);
    }
}

