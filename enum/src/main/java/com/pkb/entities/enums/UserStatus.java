package com.pkb.entities.enums;

import static java.util.Collections.unmodifiableSet;
import static java.util.EnumSet.complementOf;
import static java.util.EnumSet.of;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.concat;

import java.util.EnumSet;
import java.util.Set;

public enum UserStatus {

    NEW,
    CREATED,
    INVITED,
    EMAIL_CONFIRMED,
    ACTIVE,
    INACTIVE,
    NOCONTACT, // replace with hasContact(), if possible
    DEAD;

    public static final Set<UserStatus> PATIENT_INVITED_NOT_REGISTERED;

    public static final Set<UserStatus> PATIENT_NOT_INVITED_NOT_REGISTERED;

    public static final Set<UserStatus> PATIENT_NOT_REGISTERED;

    public static final Set<UserStatus> PATIENTS_REGISTRATION_NOT_ALLOWED;

    static {
        PATIENT_INVITED_NOT_REGISTERED = unmodifiableSet(of(CREATED, INVITED, NEW));
        PATIENT_NOT_INVITED_NOT_REGISTERED = unmodifiableSet(of(NOCONTACT));
        PATIENT_NOT_REGISTERED = unmodifiableSet(concat(PATIENT_INVITED_NOT_REGISTERED.stream(), PATIENT_NOT_INVITED_NOT_REGISTERED.stream())
                .collect(toSet()));
        PATIENTS_REGISTRATION_NOT_ALLOWED = unmodifiableSet(complementOf(EnumSet.copyOf(PATIENT_NOT_REGISTERED)));
    }

}