package com.pkb.common.testcontrol;

import com.pkb.common.testcontrol.client.TestControl;
import com.pkb.common.testcontrol.message.ImmutableClearCachesRequest;
import okhttp3.OkHttpClient;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        var tcc = TestControl.create(new OkHttpClient(), "http://localhost:9876");
        Thread.sleep(1000);
        tcc.clearCaches(ImmutableClearCachesRequest.builder().clearFixedTimestamp(false).build());
    }
}