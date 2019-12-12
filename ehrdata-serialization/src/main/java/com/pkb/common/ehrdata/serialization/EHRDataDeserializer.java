package com.pkb.common.ehrdata.serialization;

import static java.util.function.Function.identity;

import java.util.Map;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import com.pkb.common.ehrdata.deserializer.xml01.MenuDataXmlParserHelper;

public interface EHRDataDeserializer {

    @NotNull
    default Map<String, Object> deserialize(@NotNull byte[] plaintext) {
        return deserialize(plaintext, identity());
    }

    @NotNull
    default <T> T deserialize(@NotNull byte[] plaintext, @NotNull Function<Map<String, Object>, T> convert) {
        Map<String, Object> map = MenuDataXmlParserHelper.unmarshalEncryptedFields(plaintext);
        return convert.apply(map);
    }
}
