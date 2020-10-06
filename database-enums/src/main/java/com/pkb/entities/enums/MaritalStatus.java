package com.pkb.entities.enums;

public enum MaritalStatus {
    // http://hl7.org/fhir/2015Jan/valueset-marital-status.html

    Annulled("A"), //Marriage contract has been declared null and to not have existed
    Divorced("D"), //Marriage contract has been declared dissolved and inactive
    Interlocutory("I"), //	Subject to an Interlocutory Decree.
    LegallySeparated("L"), Married("M"), //	A current marriage contract is active
    Polygamous("P"), //	More than 1 current spouse
    NeverMarried("S"), //	No marriage contract has ever been entered
    DomesticPartner("T"), //	Person declares that a domestic partner relationship exists.
    Widowed("W"); //	The spouse has died

    private final String code; // to be exposed in REST

    MaritalStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
