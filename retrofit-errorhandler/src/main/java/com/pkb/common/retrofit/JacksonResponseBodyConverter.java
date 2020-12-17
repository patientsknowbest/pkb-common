package com.pkb.common.retrofit;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectReader;
import okhttp3.ResponseBody;
import retrofit2.Converter;

import java.io.IOException;
import java.util.function.Function;

final class JacksonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final ObjectReader adapter;
    private final Function<String, T> fallback;

    JacksonResponseBodyConverter(ObjectReader adapter, Function<String, T> fallback) {
        this.adapter = adapter;
        this.fallback = fallback;
    }

    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        try {
            return this.adapter.readValue(response);
        } catch (JsonParseException cause) {
            if (fallback != null) {
                return fallback.apply(response);
            }
            throw cause;
        } finally {
            value.close();
        }
    }
}
