package com.pkb.common.testcontrol.client;

import com.pkb.common.testcontrol.message.ClearStatesRequest;
import com.pkb.common.testcontrol.message.DetailedLoggingRequest;
import com.pkb.common.testcontrol.message.FixTimeRequest;
import com.pkb.common.testcontrol.message.InjectConfigRequest;
import com.pkb.common.testcontrol.message.LogTestNameRequest;
import com.pkb.common.testcontrol.message.MoveTimeRequest;
import com.pkb.common.testcontrol.message.NamespaceChangeRequest;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.PUT;

/**
 * Client for calling the test control endpoint
 */
public interface TestControl {
    static TestControl create(OkHttpClient httpClient, String testControlBaseURL) {
        return new Retrofit.Builder().baseUrl(testControlBaseURL)
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .build()
                .create(TestControl.class);
    }
    @PUT("setNamespace")
    Call<Void> setNamespace(@Body NamespaceChangeRequest request);
    @PUT("setFixedTimestamp")
    Call<Void> setFixedTimestamp(@Body FixTimeRequest request);
    @PUT("moveTime")
    Call<Void> moveTime(@Body MoveTimeRequest request);
    @PUT("injectConfig")
    Call<Void> injectConfig(@Body InjectConfigRequest request);
    @PUT("clearStates")
    Call<Void> clearStates(@Body ClearStatesRequest request);
    @PUT("logTestName")
    Call<Void> logTestName(@Body LogTestNameRequest request);
    @PUT("toggleDetailedLogging")
    Call<Void> toggleDetailedLogging(@Body DetailedLoggingRequest request);
}