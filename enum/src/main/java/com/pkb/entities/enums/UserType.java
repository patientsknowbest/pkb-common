package com.pkb.entities.enums;

public enum UserType {

    PATIENT,
    /* CLINICIAN, not supported anymore */
    REG_CLINICIAN,
    /**
     * AKA: Team Coordinator.
     */
    INSTITUTE_ADMIN,
    SUPER_ADMIN,
    EMPLOYEE,
    ORG_COORD,
    TECH_SUPPORT,
    PRIVACY_OFFICER
}

