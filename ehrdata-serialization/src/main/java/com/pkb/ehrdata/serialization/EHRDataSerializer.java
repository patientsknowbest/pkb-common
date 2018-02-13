package com.pkb.ehrdata.serialization;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

import com.pkb.ehrdata.serializer.xml01.MenuDataXmlSerializerHelper;

public interface EHRDataSerializer {

    @NotNull
    default byte[] serialize(@NotNull Map<String, Object> encryptedFields) {
        return MenuDataXmlSerializerHelper.marshalEncryptedFields(encryptedFields);
    }
}
