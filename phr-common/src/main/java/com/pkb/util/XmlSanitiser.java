package com.pkb.util;

import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

public class XmlSanitiser {

    public static final int MAX_VALUE_LENGTH = 8;

    /**
     * Sanitises XML data such that it can be written to the logs.
     * 
     * Only content inside an XML element (i.e. between an opening and closing tag) will be sanitised.
     * 
     * If null is provided, null is returned. Otherwise, the contents of tags are sanitised as follows:
     * <ul>
     * <li>Whitespace values remain</li>
     * <li>Unicode letters are replaced with: a</li>
     * <li>Numbers are replaced with: 1</li>
     * <li>Any other character is replaced with: ?</li>
     * </ul>
     * 
     * If any individual content value is longer than {@code MAX_VALUE_LENGTH} characters, then the first {@code MAX_VALUE_LENGTH}
     * characters will be returned, followed by "..."
     *  
     * @param xml The XML to be sanitised
     * @return A sanitised version of the provided value
     */
    @Nullable
    public static String sanitiseXml(String xml) {
        if(xml == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        try {
            boolean inTag = false;
            StringTokenizer tok = new StringTokenizer(xml, "><", true/*returnDelims*/);
            while (tok.hasMoreTokens()) {
                String bit = tok.nextToken();
                if (bit.equals("<")) {
                    inTag = true;
                    result.append(bit);
                } else if (bit.equals(">")) {
                    inTag = false;
                    result.append(bit);
                } else if (inTag) {
                    result.append(bit);
                } else {
                    result.append(sanitiseValue(bit));
                }
            }
        } catch (Exception e) {
            return "Failed XML sanitisation:" + e.getMessage();
        }
        return result.toString();

    }

    @Nullable
    private static String sanitiseValue(String valueText) {
        if(valueText == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < valueText.length(); i++) {
            if (i == MAX_VALUE_LENGTH) {
                result.append("...");
                break;
            }

            String s = Character.toString(valueText.charAt(i));
            if (StringUtils.isWhitespace(s)) {
                result.append(s);
            } else if (StringUtils.isAlpha(s)) {
                result.append("a");
            } else if (StringUtils.isNumeric(s)) {
                result.append("1");
            } else {
                result.append("?");
            } 
        }

        return result.toString();
    }
}
