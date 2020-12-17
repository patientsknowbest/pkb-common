package com.pkb.common.retrofit;

import io.vavr.control.Either;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ErrorHandlingCallAdapterFactory<E> extends CallAdapter.Factory {
    private final Class<E> errorType;

    public ErrorHandlingCallAdapterFactory(Class<E> errorType) {
        this.errorType = errorType;
    }

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        if (getRawType(returnType) != Either.class) {
            return null;
        }
        if (!(returnType instanceof ParameterizedType)) {
            throw new IllegalStateException("return type must have generic type (e.g., Either<SomeErrorType, SomeDto>)");
        }
        Type actualErrorType = getParameterUpperBound(0, (ParameterizedType) returnType);
        if (actualErrorType != errorType) {
            return null;
        }

        Type responseType = getParameterUpperBound(1, (ParameterizedType) returnType);
        return new ErrorHandlingCallAdapter<>(retrofit, errorType, responseType, annotations);
    }
}
