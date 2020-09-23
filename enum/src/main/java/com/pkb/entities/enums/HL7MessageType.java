package com.pkb.entities.enums;

import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public enum HL7MessageType {

    QRY_A19("QRY^A19"),
    ADT_A28("ADT^A28"),
    ADT_A31("ADT^A31"),
    ADT_A08("ADT^A08"),
    ADT_A01("ADT^A01"),
    ADT_A02("ADT^A02"),
    ADT_A03("ADT^A03"),
    ADT_A11("ADT^A11"),
    ADT_A12("ADT^A12"),
    ADT_A13("ADT^A13"),
    ADT_A05("ADT^A05"),
    ADT_A38("ADT^A38"),
    ADT_A14("ADT^A14"),
    ADT_A27("ADT^A27"),
    ORU_R01("ORU^R01"),
    MDM_T02("MDM^T02"),
    MDM_T11("MDM^T11"),
    SIU_S12("SIU^S12"),
    SIU_S13("SIU^S13"),
    SIU_S14("SIU^S14"),
    SIU_S15("SIU^S15"),
    SIU_S26("SIU^S26");

    private final String code;
    private static ImmutableMap<String, HL7MessageType> LOOKUP;

    HL7MessageType(String code) {
        this.code = code;
    }

    static {
        ImmutableMap.Builder<String, HL7MessageType> builder
                = ImmutableMap.builderWithExpectedSize(HL7MessageType.values().length);

        for (HL7MessageType messageType : HL7MessageType.values()) {
            builder.put(messageType.getCode(), messageType);
        }

        LOOKUP = builder.build();
    }


    @Nullable
    public static HL7MessageType get(@NotNull String code) {
        return LOOKUP.get(code);
    }


    public String getCode() {
        return this.code;
    }

}