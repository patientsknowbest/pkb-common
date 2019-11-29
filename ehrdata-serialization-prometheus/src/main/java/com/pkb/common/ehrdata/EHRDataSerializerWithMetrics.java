package com.pkb.common.ehrdata;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

import com.pkb.common.ehrdata.serialization.EHRDataSerializer;

public class EHRDataSerializerWithMetrics implements EHRDataSerializer {

    @NotNull
    @Override
    public byte[] serialize(@NotNull Map<String, Object> encryptedFields) {
        return serialize(encryptedFields, EHRDataXmlTimers.AUTOCLOSEABLE_XML_CREATE_SECONDS);
    }
}
