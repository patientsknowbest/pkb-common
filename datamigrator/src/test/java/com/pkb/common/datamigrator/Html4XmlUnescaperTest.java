package com.pkb.common.datamigrator;

import static com.github.karsaig.approvalcrest.jupiter.MatcherAssert.assertThat;
import static com.github.karsaig.approvalcrest.jupiter.matcher.Matchers.sameBeanAs;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.apache.commons.text.StringEscapeUtils;
import org.junit.jupiter.api.Test;


public class Html4XmlUnescaperTest {

    private static final String UNESCAPED_SUBJECT = "SUBJECT <tag>'&\"</tag>";
    private static final String ESCAPED_SUBJECT = StringEscapeUtils.escapeXml10(StringEscapeUtils.escapeHtml4(UNESCAPED_SUBJECT));

    private Html4XmlUnescaper underTest = new Html4XmlUnescaper();

    @Test
    public void shouldReturnNullWhenNull() {
        //GIVEN - Input is null

        //WHEN
        String actual = underTest.unescape(null);

        //THEN
        assertNull(actual);
    }

    @Test
    public void shouldUnescapeWhenEscapedString() {
        //GIVEN

        //WHEN
        String actual = underTest.unescape(ESCAPED_SUBJECT);

        //THEN
        assertThat(actual, sameBeanAs(UNESCAPED_SUBJECT));
    }

    @Test
    public void shouldNotChangeUnescapedStringWhenUnescapedString() {
        //GIVEN

        //WHEN
        String actual = underTest.unescape(UNESCAPED_SUBJECT);

        //THEN
        assertThat(actual, sameBeanAs(UNESCAPED_SUBJECT));
    }
}