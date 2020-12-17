package com.pkb.common.retrofit;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public final class JacksonConverterFactory extends Converter.Factory {
    private final ObjectMapper mapper;
    private final Function<String, ?> errorResponseFallbackHandler;

    public static JacksonConverterFactory create() {
        return create(new ObjectMapper());
    }

    public static JacksonConverterFactory create(ObjectMapper mapper) {
        return create(new ObjectMapper(), null);
    }

    public static JacksonConverterFactory create(ObjectMapper mapper, Function<String, ?> errorResponseFallbackHandler) {
        return new JacksonConverterFactory(requireNonNull(mapper, "mapper == null"), errorResponseFallbackHandler);
    }

    private JacksonConverterFactory(ObjectMapper mapper, Function<String, ?> errorResponseFallbackHandler) {
        this.mapper = mapper;
        this.errorResponseFallbackHandler = errorResponseFallbackHandler;
    }

    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        JavaType javaType = this.mapper.getTypeFactory().constructType(type);
        ObjectReader reader = this.mapper.readerFor(javaType);
        return new JacksonResponseBodyConverter<>(reader, errorResponseFallbackHandler);
    }

    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        JavaType javaType = this.mapper.getTypeFactory().constructType(type);
        ObjectWriter writer = this.mapper.writerFor(javaType);
        return new JacksonRequestBodyConverter<>(writer);
    }
}
