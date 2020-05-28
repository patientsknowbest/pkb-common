package com.pkb.util;

import javax.security.auth.Subject;

import com.pkb.crypto.UserPrivateKey;
import com.pkb.crypto.UserPublicKey;

public final class SubjectUtils {

    private SubjectUtils() {
    }

    /**
     * Use this only for FHIR as FHIR stores both {@link UserPublicKey} & {@link UserPrivateKey} in {@link Subject#getPrivateCredentials()}
     * while PHR stores {@link UserPublicKey} in {@link Subject#getPublicCredentials()}.
     * 
     * @param subject
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T getPrivateCredential(Subject subject, Class<T> cls) {
        return subject.getPrivateCredentials(cls).stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Private credential not found for class " + cls));
    }

}
