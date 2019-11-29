package com.pkb.common.ehrdata;

import java.util.Map;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import com.pkb.common.ehrdata.serialization.EHRDataDeserializer;

public class EHRDataDeserializerWithMetrics implements EHRDataDeserializer {

    @NotNull
    @Override
    public <T> T deserialize(@NotNull byte[] plaintext, @NotNull Function<Map<String, Object>, T> convert) {
        return deserialize(plaintext, convert, EHRDataXmlTimers.AUTOCLOSEABLE_XML_PARSE_SECONDS);
    }

}