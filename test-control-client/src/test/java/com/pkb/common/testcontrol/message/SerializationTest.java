package com.pkb.common.testcontrol.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.karsaig.approvalcrest.jupiter.MatcherAssert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static com.github.karsaig.approvalcrest.jupiter.matcher.Matchers.sameBeanAs;

public class SerializationTest {
    static Object[][] testCases() {
        return new Object[][]{
                {ClearInternalStateRequest.class, ImmutableClearInternalStateRequest.builder().clearFixedTimestamp(false).build()},
                {ClearStorageRequest.class, ImmutableClearStorageRequest.builder().build()},
                {DetailedLoggingRequest.class, ImmutableDetailedLoggingRequest.builder().enableDetailedLogging(false).build()},
                {FixTimeRequest.class, ImmutableFixTimeRequest.builder().timestamp(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(ZonedDateTime.now())).build()},
                {InjectConfigRequest.class, ImmutableInjectConfigRequest.builder().key("foo").value("bar").build()},
                {InjectConfigRequest.class, ImmutableInjectConfigRequest.builder().key("nofoo").value(null).build()},
                {LogTestNameRequest.class, ImmutableLogTestNameRequest.builder().testName("zoo").build()},
                {MoveTimeRequest.class, ImmutableMoveTimeRequest.builder().amount(1).unit("foo").build()},
                {NamespaceChangeRequest.class, ImmutableNamespaceChangeRequest.builder().newNamespace("qoo").build()},
                {Startup.class, ImmutableStartup.builder().name("doo").callback("woo").build()},
        };
    }
    @ParameterizedTest
    @MethodSource("testCases")
    <T> void serializationTest(Class<T> clazz, T in) throws IOException {
        var om = new ObjectMapper();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        om.writerFor(clazz)
            .writeValue(baos, in);
        T out = om.readerFor(clazz)
                .readValue(new ByteArrayInputStream(baos.toByteArray()));
        MatcherAssert.assertThat(in, sameBeanAs(out));
    }
}
