package com.pkb.common.ehrdata.serializer.xml01;

import static com.github.karsaig.approvalcrest.MatcherAssert.assertThat;
import static com.github.karsaig.approvalcrest.matcher.Matchers.sameBeanAs;
import static io.vavr.API.unchecked;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.base.Charsets;
import com.pkb.common.ehrdata.deserializer.xml01.MenuDataXmlParserHelper;
import com.pkb.common.ehrdata.deserializer.xml01.MenuDataXmlParserHelperTest;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

@RunWith(Parameterized.class)
public class MenuDataXmlSerializerHelperTest {

    private final byte[] expectedBytes;

    public MenuDataXmlSerializerHelperTest(String resourceFilename) {
        this.expectedBytes = unchecked(() -> loadResourceXml()).get().getBytes();
    }

    @Parameters
    public static List<Object[]> params() {
        return Arrays.asList(
                new Object[]{"diagnosis.xml"},
                new Object[]{"allergy.xml"},
                new Object[]{"symptom.xml"},
                new Object[]{"measurement.xml"},
                new Object[]{"test.xml"},
                new Object[]{"message.xml"},
                new Object[]{"appointment.xml"}
        );
    }

    @Test
    public void testDataCanBeSerialised() {
        // GIVEN
        Map<String, Object> expected = MenuDataXmlParserHelper.unmarshalEncryptedFields(expectedBytes);

        // WHEN
        byte[] marshalled = MenuDataXmlSerializerHelper.marshalEncryptedFields(expected);
        Map<String, Object> actual = MenuDataXmlParserHelper.unmarshalEncryptedFields(marshalled);

        // THEN
        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void unmarshalEncryptedFields_serializeTimerIsUsed() {
        // GIVEN
        SimpleMeterRegistry smr = new SimpleMeterRegistry();
        Metrics.globalRegistry.add(smr);
        Map<String, Object> expected = MenuDataXmlParserHelper.unmarshalEncryptedFields(expectedBytes);

        // WHEN
        byte[] marshalled = MenuDataXmlSerializerHelper.marshalEncryptedFields(expected);
        MenuDataXmlParserHelper.unmarshalEncryptedFields(marshalled);

        // THEN
        long xmlCreateTimerCount = smr.timer("pkb_phr_menudata_xmlcreateseconds").count();
        assertThat(xmlCreateTimerCount, is(1L));
    }

    private static String loadResourceXml() throws IOException {
        try (InputStream inputStream = MenuDataXmlParserHelperTest.class.getResourceAsStream("diagnosis.xml")) {
            return IOUtils.toString(inputStream, Charsets.UTF_8);
        }
    }
}