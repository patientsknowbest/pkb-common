package com.pkb.common.datamigrator;

import static org.apache.commons.text.StringEscapeUtils.unescapeHtml4;
import static org.apache.commons.text.StringEscapeUtils.unescapeXml;

import org.jetbrains.annotations.Nullable;

public class Html4XmlUnescaper {

    public @Nullable String unescape(@Nullable String content) {
        return unescapeHtml4(unescapeXml(content));
    }
}
