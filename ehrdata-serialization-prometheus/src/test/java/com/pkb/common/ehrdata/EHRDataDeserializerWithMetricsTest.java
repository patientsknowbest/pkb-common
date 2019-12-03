package com.pkb.common.ehrdata;

import static com.pkb.common.ehrdata.EHRDataXmlTimers.AUTOCLOSEABLE_XML_PARSE_SECONDS;
import static java.util.function.Function.identity;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class EHRDataDeserializerWithMetricsTest {

    @Test
    public void deserialize_passesPrometheusTimer() {
        //given
        Map<String, Object> map = ImmutableMap.of("patientId", "1772");
        EHRDataDeserializerWithMetrics ehrDataSerializerWithMetrics = spy(new EHRDataDeserializerWithMetrics());
        byte[] serialized = new byte[1];
        doReturn(map).when(ehrDataSerializerWithMetrics).deserialize(serialized, identity(), AUTOCLOSEABLE_XML_PARSE_SECONDS);

        //when
        Map<String, Object> deserializedMap = ehrDataSerializerWithMetrics.deserialize(serialized);

        //then
        assertThat(deserializedMap, is(map));
    }
}