package com.pkb.common.ehrdata.deserializer.xml01;

import static com.github.karsaig.approvalcrest.MatcherAssert.assertThat;
import static com.github.karsaig.approvalcrest.matcher.Matchers.sameBeanAs;
import static io.vavr.API.unchecked;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

public class MenuDataXmlParserHelperTest {

    private static final String DIAGNOSIS = unchecked(() -> loadResourceXml("diagnosis.xml")).get();
    private static final String ALLERGY = unchecked(() -> loadResourceXml("allergy.xml")).get();
    private static final String SYMPTOM = unchecked(() -> loadResourceXml("symptom.xml")).get();
    private static final String MEASUREMENT = unchecked(() -> loadResourceXml("measurement.xml")).get();
    private static final String TEST = unchecked(() -> loadResourceXml("test.xml")).get();
    private static final String MESSAGE = unchecked(() -> loadResourceXml("message.xml")).get();
    private static final String APPOINTMENT = unchecked(() -> loadResourceXml("appointment.xml")).get();

    @Test
    public void testDiagnosisCanBeParsed() {

        // GIVEN
        Map<String, Object> coding = Maps.newHashMap();
        coding.put("codings", null);
        coding.put("displayText", "Fear of heights");

        Map<String, Object> expected = Maps.newHashMap();
        expected.put("severity", null);
        expected.put("sourceText", null);
        expected.put("diagnosis", coding);
        expected.put("episodicity", null);
        expected.put("severityCodeId", null);
        expected.put("anatomicalLocations", null);
        expected.put("onset", null);
        expected.put("clinicalStatus", null);
        expected.put("bodySite", null);
        expected.put("asserter", null);
        expected.put("stage", null);
        expected.put("authorLdapUID", "1772");
        expected.put("hospitalServiceCode", null);
        expected.put("episodicityCodeId", null);
        expected.put("course", null);
        expected.put("details", "Dizziness");
        expected.put("category", null);
        expected.put("abatement", null);
        expected.put("status", null);

        // WHEN
        Map<String, Object> actual = MenuDataXmlParserHelper.unmarshalEncryptedFields(DIAGNOSIS.getBytes());

        // THEN
        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void testAllergyCanBeParsed() {

        // GIVEN
        Map<String, Object> allergenCodings = Maps.newHashMap();
        allergenCodings.put("codings", null);
        allergenCodings.put("displayText", "Dairy");

        Map<String, Object> reactionCodings = Maps.newHashMap();
        reactionCodings.put("codings", Lists.newArrayList(ImmutableMap.of(
                "code", "422587007",
                "codeSystem", "SNOMED-CT",
                "displayText", "Nausea")));
        reactionCodings.put("displayText", "Nausea");

        Map<String, Object> severityCodings = Maps.newHashMap();
        severityCodings.put("codings", Lists.newArrayList(ImmutableMap.of(
                "code", "255604002",
                "codeSystem", "SNOMED-CT",
                "displayText", "Mild")));
        severityCodings.put("displayText", "Mild");

        Map<String, Object> expected = Maps.newHashMap();
        expected.put("severity", severityCodings);
        expected.put("allergen", allergenCodings);
        expected.put("disorder", null);
        expected.put("endOfSymptoms", null);
        expected.put("authorLdapUID", "1772");
        expected.put("hospitalServiceCode", null);
        expected.put("sourceText", null);
        expected.put("reactions", Lists.newArrayList(reactionCodings));
        expected.put("status", null);

        // WHEN
        Map<String, Object> actual = MenuDataXmlParserHelper.unmarshalEncryptedFields(ALLERGY.getBytes());

        // THEN
        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void testSymptomCanBeParsed() {
        // GIVEN
        Map<String, Object> expected = Maps.newHashMap();
        expected.put("comments", null);
        expected.put("hospitalServiceCode", null);
        expected.put("sourceText", null);
        expected.put("dataEntererUid", null);
        expected.put("severityPrivateId", "1");
        expected.put("symptomPrivateId", "2070");

        // WHEN
        Map<String, Object> actual = MenuDataXmlParserHelper.unmarshalEncryptedFields(SYMPTOM.getBytes());

        // THEN
        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void testMeasurementCanBeParsed() {
        // GIVEN
        Map<String, Object> expected = Maps.newHashMap();
        expected.put("value2", null);
        expected.put("hospitalServiceCode", null);
        expected.put("durationValue", null);
        expected.put("sourceText", null);
        expected.put("valueNonNumeric", null);
        expected.put("durationUnit", null);
        expected.put("measurementTypeId", "20");
        expected.put("value", 150.0);

        // WHEN
        Map<String, Object> actual = MenuDataXmlParserHelper.unmarshalEncryptedFields(MEASUREMENT.getBytes());

        // THEN
        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void testTestResultCanBeParsed() {
        // GIVEN
        Map<String, Object> expected = Maps.newHashMap();
        expected.put("comments", null);
        expected.put("loincTestId", "1903");
        expected.put("textValue", null);
        expected.put("rangeLow", 20.0);
        expected.put("sourceText", null);
        expected.put("sourceFileId", null);
        expected.put("deletedByPersonId", null);
        expected.put("serviceNameFromSource", null);
        expected.put("rangeHigh", 50.0);
        expected.put("rangeHighInclusive", true);
        expected.put("hospitalServiceCode", null);
        expected.put("textRange", null);
        expected.put("testCodingSystemFromSource", null);
        expected.put("rangeLowInclusive", true);
        expected.put("delayDisplayDays", null);
        expected.put("testCodeFromSource", null);
        expected.put("value", 100.0);
        expected.put("dataEntererId", null);

        // WHEN
        Map<String, Object> actual = MenuDataXmlParserHelper.unmarshalEncryptedFields(TEST.getBytes());

        // THEN
        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void testMessageCanBeParsed() {
        // GIVEN
        Map<String, Object> expected = Maps.newHashMap();
        expected.put("participantsNonPKB", null);
        expected.put("onBehalfAuthorId", null);
        expected.put("htmlAllowed", false);
        expected.put("subject", "Test message");
        expected.put("sourceText", null);
        expected.put("content", "Saying hello");
        expected.put("encounterStatus", null);
        expected.put("hospitalServiceCode", null);
        expected.put("location", null);
        expected.put("reasonCode", null);
        expected.put("indication", null);
        expected.put("delayDisplayDays", null);
        expected.put("participants", Lists.newArrayList("1622", "1772"));

        // WHEN
        Map<String, Object> actual = MenuDataXmlParserHelper.unmarshalEncryptedFields(MESSAGE.getBytes());

        // THEN
        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void testAppointmentCanBeParsed() {
        // GIVEN
        Map<String, Object> expected = Maps.newHashMap();
        expected.put("patientId", "1772");
        expected.put("subject", "Test appointment");
        expected.put("hospitalServiceCode", null);
        expected.put("sourceText", null);
        expected.put("description", "Test appointment");
        expected.put("location", "Surgery");

        // WHEN
        Map<String, Object> actual = MenuDataXmlParserHelper.unmarshalEncryptedFields(APPOINTMENT.getBytes());

        // THEN
        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void unmarshalEncryptedFields_xmlParseTimerIsUsed() {
        // GIVEN
        SimpleMeterRegistry smr = new SimpleMeterRegistry();
        Metrics.globalRegistry.add(smr);

        // WHEN
        Map<String, Object> actual = MenuDataXmlParserHelper.unmarshalEncryptedFields(APPOINTMENT.getBytes());

        // THEN
        long xmlCreateTimerCount = smr.timer("pkb_phr_menudata_xmlparseseconds").count();
        assertThat(xmlCreateTimerCount, is(1L));
    }

    private static String loadResourceXml(String filename) throws IOException {
        try (InputStream inputStream = MenuDataXmlParserHelperTest.class.getResourceAsStream(filename)) {
            return IOUtils.toString(inputStream, Charsets.UTF_8);
        }
    }
}