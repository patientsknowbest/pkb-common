package com.pkb.common.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.pkb.util.XmlSanitiser;

public class XmlSanitiserTest {

    @Test
    public void testSanitiseXmlNull() {
        // GIVEN
        // WHEN
        String xml = null;
        // THEN
        assertNull(XmlSanitiser.sanitiseXml(xml));
    }

    @Test
    public void testSanitiseXmlEmpty() {
        // GIVEN
        // WHEN
        String xml = "";
        // THEN
        assertEquals("", XmlSanitiser.sanitiseXml(xml));
    }

    @Test
    public void testSanitiseXmlBlank() {
        // GIVEN
        // WHEN
        String xml = " ";
        // THEN
        assertEquals(" ", XmlSanitiser.sanitiseXml(xml));
    }

    @Test
    public void testSanitiseXmlSingleTagLeft() {
        // GIVEN
        // WHEN
        String xml = "<";
        // THEN
        assertEquals("<", XmlSanitiser.sanitiseXml(xml));
    }

    @Test
    public void testSanitiseXmlSingleTagRight() {
        // GIVEN
        // WHEN
        String xml = ">";
        // THEN
        assertEquals(">", XmlSanitiser.sanitiseXml(xml));
    }

    @Test
    public void testSanitiseXmlSingleTagLeftEscaped() {
        // GIVEN
        // WHEN
        String xml = "&lt;";
        // THEN
        assertEquals("?aa?", XmlSanitiser.sanitiseXml(xml));
    }

    @Test
    public void testSanitiseXmlSingleTagRightEscaped() {
        // GIVEN
        // WHEN
        String xml = "&gt;";
        // THEN
        assertEquals("?aa?", XmlSanitiser.sanitiseXml(xml));
    }

    @Test
    public void testSanitiseXmlSingleTag() {
        // GIVEN
        // WHEN
        String xml = "<one>";
        // THEN
        assertEquals("<one>", XmlSanitiser.sanitiseXml(xml));
    }

    @Test
    public void testSanitiseXmlSingleTagClosed() {
        // GIVEN
        // WHEN
        String xml = "<one></one>";
        // THEN
        assertEquals("<one></one>", XmlSanitiser.sanitiseXml(xml));
    }

    @Test
    public void testSanitiseXmlSingleTagSelfClosed() {
        // GIVEN
        // WHEN
        String xml = "<one/>";
        // THEN
        assertEquals("<one/>", XmlSanitiser.sanitiseXml(xml));
    }

    @Test
    public void testSanitiseXmlSingleTagWithContentLetterMatchPlaceholder() {
        // GIVEN
        // WHEN
        String xml = "<one>a</one>";
        // THEN
        assertEquals("<one>a</one>", XmlSanitiser.sanitiseXml(xml));
    }

    @Test
    public void testSanitiseXmlSingleTagWithContentLetterNoMatchPlaceholder() {
        // GIVEN
        // WHEN
        String xml = "<one>b</one>";
        // THEN
        assertEquals("<one>a</one>", XmlSanitiser.sanitiseXml(xml));
    }

    @Test
    public void testSanitiseXmlSingleTagWithContentNumberMatchPlaceholder() {
        // GIVEN
        // WHEN
        String xml = "<one>1</one>";
        // THEN
        assertEquals("<one>1</one>", XmlSanitiser.sanitiseXml(xml));
    }

    @Test
    public void testSanitiseXmlSingleTagWithContentNumberNoMatchPlaceholder() {
        // GIVEN
        // WHEN
        String xml = "<one>2</one>";
        // THEN
        assertEquals("<one>1</one>", XmlSanitiser.sanitiseXml(xml));
    }

    @Test
    public void testSanitiseXmlSingleTagWithContentSpecialMatchPlaceholder() {
        // GIVEN
        // WHEN
        String xml = "<one>?</one>";
        // THEN
        assertEquals("<one>?</one>", XmlSanitiser.sanitiseXml(xml));
    }

    @Test
    public void testSanitiseXmlSingleTagWithContentSpecialNoMatchPlaceholder() {
        // GIVEN
        // WHEN
        String xml = "<one>@</one>";
        // THEN
        assertEquals("<one>?</one>", XmlSanitiser.sanitiseXml(xml));
    }

    @Test
    public void testSanitiseXmlSingleTagWithContentMixed() {
        // GIVEN
        // WHEN
        String xml = "<one>£-99_XYZ</one>";
        // THEN
        assertEquals("<one>??11?aaa</one>", XmlSanitiser.sanitiseXml(xml));
    }

    @Test
    public void testSanitiseXmlSingleTagWithContentMixedTruncated() {
        // GIVEN
        // WHEN
        String xml = "<one>£-99_XYZ.</one>";
        // THEN
        assertEquals("<one>??11?aaa...</one>", XmlSanitiser.sanitiseXml(xml));
    }

    @Test
    public void testSanitiseXmlTwoTags() {
        // GIVEN
        // WHEN
        String xml = "<one></one><two></two>";
        // THEN
        assertEquals("<one></one><two></two>", XmlSanitiser.sanitiseXml(xml));
    }

    @Test
    public void testSanitiseXmlTwoTagsWithContent() {
        // GIVEN
        // WHEN
        String xml = "<one>CONT_1</one><two>CONTENT_2</two>";
        // THEN
        assertEquals("<one>aaaa?1</one><two>aaaaaaa?...</two>", XmlSanitiser.sanitiseXml(xml));
    }

    @Test
    public void testSanitiseXmlNestedTags() {
        // GIVEN
        // WHEN
        String xml = "<one><two></two></one>";
        // THEN
        assertEquals("<one><two></two></one>", XmlSanitiser.sanitiseXml(xml));
    }

    @Test
    public void testSanitiseXmlNestedTagsWithContent() {
        // GIVEN
        // WHEN
        String xml = "<one><two>abc123%</two></one>";
        // THEN
        assertEquals("<one><two>aaa111?</two></one>", XmlSanitiser.sanitiseXml(xml));
    }

    @Test
    public void testSanitiseXmlSingleTagWith1Attribute() {
        // GIVEN
        // WHEN
        String xml = "<one a=\"1\"></one>";
        // THEN
        assertEquals("<one a=\"1\"></one>", XmlSanitiser.sanitiseXml(xml));
    }

    @Test
    public void testSanitiseXmlSingleTagWith2Attributes() {
        // GIVEN
        // WHEN
        String xml = "<one a=\"1\"  b = \"59.1px\"></one>";
        // THEN
        assertEquals("<one a=\"1\"  b = \"59.1px\"></one>", XmlSanitiser.sanitiseXml(xml));
    }

    @Test
    public void testSanitiseXmlSingleTagWithAttributeAndContent() {
        // GIVEN
        // WHEN
        String xml = "<one key=\"anAcceptableAttribute\">1/2 of all your tests were good.</one>";
        // THEN
        assertEquals("<one key=\"anAcceptableAttribute\">1?1 aa a...</one>", XmlSanitiser.sanitiseXml(xml));
    }

    @Test
    public void testSanitiseXmlProlog() {
        // GIVEN
        // WHEN
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        // THEN
        assertEquals(xml, XmlSanitiser.sanitiseXml(xml));
    }

    @Test
    public void testSanitiseXmlRealisticHl7PidSegment() {
        // GIVEN
        String orig = "<PID><PID.1></PID.1><PID.2><PID.2.1>9944474428</PID.2.1><PID.2.2></PID.2.2><PID.2.3>NHS</PID.2.3></PID.2><PID.3><PID.3.1>NWP01</PID.3.1><PID.3.2></PID.3.2><PID.3.3>HOSP</PID.3.3></PID.3><PID.4></PID.4><PID.5><PID.5.1>penguin</PID.5.1><PID.5.2>tuesday</PID.5.2></PID.5><PID.6></PID.6><PID.7><PID.7.1>19500101</PID.7.1></PID.7><PID.8><PID.8.1>M</PID.8.1></PID.8></PID>";
        String sanitised = "<PID><PID.1></PID.1><PID.2><PID.2.1>11111111...</PID.2.1><PID.2.2></PID.2.2><PID.2.3>aaa</PID.2.3></PID.2><PID.3><PID.3.1>aaa11</PID.3.1><PID.3.2></PID.3.2><PID.3.3>aaaa</PID.3.3></PID.3><PID.4></PID.4><PID.5><PID.5.1>aaaaaaa</PID.5.1><PID.5.2>aaaaaaa</PID.5.2></PID.5><PID.6></PID.6><PID.7><PID.7.1>11111111</PID.7.1></PID.7><PID.8><PID.8.1>a</PID.8.1></PID.8></PID>";
        // WHEN
        String xml = orig;
        // THEN
        assertEquals(sanitised, XmlSanitiser.sanitiseXml(xml));
    }
}