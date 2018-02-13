package com.pkb.ehrdata.serialization;

import java.util.Map;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import com.pkb.ehrdata.deserializer.xml01.MenuDataXmlParserHelper;

public interface EHRDataDeserializer {

    @NotNull
    default <T> T deserialize(@NotNull byte[] plaintext, @NotNull Function<Map<String, Object>, T> convert) {

        Map<String, Object> map = MenuDataXmlParserHelper.unmarshalEncryptedFields(plaintext);
        return convert.apply(map);
    }
}
