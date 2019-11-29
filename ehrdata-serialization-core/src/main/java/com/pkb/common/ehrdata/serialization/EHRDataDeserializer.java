package com.pkb.common.ehrdata.serialization;

import static com.pkb.common.ehrdata.NoOpAutocloseableTimer.NO_OP_TIMER;
import static java.util.function.Function.identity;

import java.util.Map;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import com.pkb.common.ehrdata.AutocloseableTimer;
import com.pkb.common.ehrdata.deserializer.xml01.MenuDataXmlParserHelper;

public interface EHRDataDeserializer {

    @NotNull
    default Map<String, Object> deserialize(@NotNull byte[] plaintext) {
        return deserialize(plaintext, identity());
    }

    @NotNull
    default <T> T deserialize(@NotNull byte[] plaintext, @NotNull Function<Map<String, Object>, T> convert) {
        return deserialize(plaintext, convert, NO_OP_TIMER);
    }

    @NotNull
    default <T> T deserialize(@NotNull byte[] plaintext, @NotNull Function<Map<String, Object>, T> convert, AutocloseableTimer xmlParseTimer) {
        Map<String, Object> map = MenuDataXmlParserHelper.unmarshalEncryptedFields(plaintext, xmlParseTimer);
        return convert.apply(map);
    }
}
