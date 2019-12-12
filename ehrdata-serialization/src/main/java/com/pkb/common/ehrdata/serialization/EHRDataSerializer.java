package com.pkb.common.ehrdata.serialization;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

import com.pkb.common.ehrdata.serializer.xml01.MenuDataXmlSerializerHelper;

public interface EHRDataSerializer {
    @NotNull
    default byte[] serialize(@NotNull Map<String, Object> encryptedFields) {
        return MenuDataXmlSerializerHelper.marshalEncryptedFields(encryptedFields);
    }
}
