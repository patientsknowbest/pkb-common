package com.pkb.common.testcontrol.client;

import com.pkb.common.testcontrol.message.ClearInternalStateRequest;
import com.pkb.common.testcontrol.message.ClearStorageRequest;
import com.pkb.common.testcontrol.message.DetailedLoggingRequest;
import com.pkb.common.testcontrol.message.FixTimeRequest;
import com.pkb.common.testcontrol.message.InjectConfigRequest;
import com.pkb.common.testcontrol.message.LogTestNameRequest;
import com.pkb.common.testcontrol.message.MoveTimeRequest;
import com.pkb.common.testcontrol.message.ResumeProcessingRequest;
import com.pkb.common.testcontrol.message.SuspendProcessingRequest;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.PUT;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * Client for calling the test control endpoint
 * 
 * `void` return type isn't allowed by retrofit, so I've used Void which is 
 * just a box for the same thing.
 */
public interface TestControl {
    String IO_PKB_TESTCONTROL_PREFIX = "io-pkb-testcontrol-";

    static TestControl create(OkHttpClient httpClient, String testControlBaseURL) {
        return new Retrofit.Builder().baseUrl(testControlBaseURL)
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(new ErrorThrowingVoidCallAdapterFactory())
                .build()
                .create(TestControl.class);
    }

    @PUT(IO_PKB_TESTCONTROL_PREFIX + "setFixedTimestamp")
    Void setFixedTimestamp(@Body FixTimeRequest request);
    @PUT(IO_PKB_TESTCONTROL_PREFIX + "moveTime")
    Void moveTime(@Body MoveTimeRequest request);
    @PUT(IO_PKB_TESTCONTROL_PREFIX + "injectConfig")
    Void injectConfig(@Body InjectConfigRequest request);
    @PUT(IO_PKB_TESTCONTROL_PREFIX + "clearInternalState")
    Void clearInternalState(@Body ClearInternalStateRequest request);
    @PUT(IO_PKB_TESTCONTROL_PREFIX + "clearStorage")
    Void clearStorage(@Body ClearStorageRequest request);
    @PUT(IO_PKB_TESTCONTROL_PREFIX + "logTestName")
    Void logTestName(@Body LogTestNameRequest request);
    @PUT(IO_PKB_TESTCONTROL_PREFIX + "toggleDetailedLogging")
    Void toggleDetailedLogging(@Body DetailedLoggingRequest request);
    @PUT(IO_PKB_TESTCONTROL_PREFIX + "suspendProcessing")
    Void suspendProcessing(@Body SuspendProcessingRequest request);
    @PUT(IO_PKB_TESTCONTROL_PREFIX + "resumeProcessing")
    Void resumeProcessing(@Body ResumeProcessingRequest request);
}


/**
 * Adapt Void calls to 
 * 1. Synchronously execute them, we're not interested in asynchronicity here
 * 2. Throw runtime exceptions if anything failed
 */
class ErrorThrowingVoidCallAdapterFactory extends CallAdapter.Factory {
    @Override
    @Nullable
    public  CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        if (returnType != Void.class) {
            return null;
        }
        return new ErrorThrowingVoidCallAdapter();
    }
}

class ErrorThrowingVoidCallAdapter implements CallAdapter<Void, Void> {
    @NotNull
    @Override
    public Type responseType() {
        return Void.class;
    }
    @Nullable
    @Override
    public Void adapt(Call<Void> call) {
        Response<Void> response;
        try {
            response = call.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (!response.isSuccessful()) {
            var errorStr = Optional.ofNullable(response.errorBody())
                    .map(eb -> {
                        try {
                            return eb.string();
                        } catch (IOException ioe) {
                            return "Error getting error body " + ioe.getMessage();
                        }
                    })
                    .orElse("unknown");
            throw new RuntimeException("Test support call failed: " + errorStr);
        }
        return null;
    }
}