package com.pkb.common.ehrdata;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class EHRDataSerializerWithMetricsTest {

    @Test
    public void serialize_passesPrometheusTimer() {
        //given
        Map<String, Object> map = ImmutableMap.of("patientId", "1772");
        EHRDataSerializerWithMetrics ehrDataSerializerWithMetrics = spy(new EHRDataSerializerWithMetrics());
        byte[] serializeResult = new byte[1];
        doReturn(serializeResult).when(ehrDataSerializerWithMetrics).serialize(map, EHRDataXmlTimers.AUTOCLOSEABLE_XML_CREATE_SECONDS);

        //when
        byte[] serialized = ehrDataSerializerWithMetrics.serialize(map);

        //then
        assertThat(serialized, is(serializeResult));
    }

}