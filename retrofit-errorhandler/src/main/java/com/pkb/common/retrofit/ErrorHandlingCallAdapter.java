package com.pkb.common.retrofit;

import io.vavr.control.Either;
import io.vavr.control.Try;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.function.BiFunction;

import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;
import static io.vavr.control.Try.success;

public class ErrorHandlingCallAdapter<E, R> implements CallAdapter<R, Either<E, R>> {
    private final Retrofit retrofit;
    private final Type errorType;
    private final Type responseType;
    private final Annotation[] annotations;

    public ErrorHandlingCallAdapter(Retrofit retrofit, Type errorType, Type responseType, Annotation[] annotations) {
        this.retrofit = retrofit;
        this.errorType = errorType;
        this.responseType = responseType;
        this.annotations = annotations;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public Either<E, R> adapt(Call<R> call) {
        try {
            Response<R> response = call.execute();
            if (response.isSuccessful()) {
                return right(response.body());
            } else {
                return left(retrofit.<E> responseBodyConverter(errorType, annotations)
                        .convert(response.errorBody()));
            }
        } catch (IOException cause) {
            throw new IllegalStateException(cause);
        }
    }
}
