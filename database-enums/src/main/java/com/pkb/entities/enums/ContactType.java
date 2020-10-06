package com.pkb.entities.enums;

public enum ContactType {
    EMAIL,
    /**
     * Not used at the moment. PublicPkbPerson.phone is used instead. Migration is taken into account.
     */
    PHONE,
    /**
     * PersonContact having given type stands for relationship between a patient and a carer.
     * (Theoretically) Carer ( PublicPKBPerson.refPerson) should be notified respectively. Important: It seems given type has been superseded by other mechanism and it is going to
     * be tidy up in the near future.
     */
    ALSO_CONTACT
}