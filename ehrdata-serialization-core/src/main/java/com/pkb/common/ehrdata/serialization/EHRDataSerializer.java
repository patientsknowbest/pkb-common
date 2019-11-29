package com.pkb.common.ehrdata.serialization;

import static com.pkb.common.ehrdata.NoOpAutocloseableTimer.NO_OP_TIMER;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

import com.pkb.common.ehrdata.AutocloseableTimer;
import com.pkb.common.ehrdata.serializer.xml01.MenuDataXmlSerializerHelper;

public interface EHRDataSerializer {
    @NotNull
    default byte[] serialize(@NotNull Map<String, Object> encryptedFields) {
        return serialize(encryptedFields, NO_OP_TIMER);
    }

    @NotNull
    default byte[] serialize(@NotNull Map<String, Object> encryptedFields, AutocloseableTimer xmlCreationTimer) {
        return MenuDataXmlSerializerHelper.marshalEncryptedFields(encryptedFields, xmlCreationTimer);
    }
}
