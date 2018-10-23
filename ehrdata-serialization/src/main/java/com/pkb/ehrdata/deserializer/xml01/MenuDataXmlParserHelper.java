package com.pkb.ehrdata.deserializer.xml01;

import static com.google.common.base.Preconditions.checkState;
import static io.vavr.API.unchecked;
import static io.vavr.Function1.identity;
import static java.util.stream.Collectors.toMap;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.prometheus.client.Summary;
import io.vavr.Function1;
import io.vavr.Function2;

public class MenuDataXmlParserHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final Summary XML_PARSE_SECONDS = Summary.build()
            .name("pkb_phr_menudata_xmlparseseconds")
            .help("Time needed to parse xml01")
            .quantile(0.5, 0.05)
            .quantile(0.9, 0.01)
            .quantile(0.95, 0.005)
            .quantile(0.99, 0.001)
            .register();

    private static final XMLInputFactory XML_INPUT_FACTORY = XMLInputFactory.newInstance();
    private static final String CDATA_ENCODING_ATTRIB = "cdataEncoding";
    private static final String CDATA_ENCODING_BASE64 = "Base64";

    private MenuDataXmlParserHelper() {}

    @NotNull
    public static Map<String, Object> unmarshalEncryptedFields(byte[] plaintext) {

        if (ArrayUtils.isNotEmpty(plaintext)) {
            return unchecked(() -> {
                XMLStreamReader reader = null;
                try (Summary.Timer ignored = XML_PARSE_SECONDS.startTimer()) {
                    reader = createXMLStreamReader(plaintext);
                    return readXmlFields(reader);
                } finally {
                    if (reader != null) {
                        reader.close();
                    }
                }
            }).get();
        }

        return new HashMap<>();
    }

    @NotNull
    private static XMLStreamReader createXMLStreamReader(byte[] plaintext) throws XMLStreamException, IOException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(plaintext)) {
            return XML_INPUT_FACTORY.createXMLStreamReader(bais, "UTF-8");
        }
    }

    @NotNull
    private static Map<String, Object> readXmlFields(XMLStreamReader reader) throws XMLStreamException {
        Map<String, Object> loadMap = new HashMap<>();
        readXmlFields(reader, loadMap);
        return loadMap;
    }

    private static void readXmlFields(XMLStreamReader reader, Map<String, Object> loadMap) throws XMLStreamException {

        boolean cdataIsBase64 = false;

        while (reader.hasNext()) {
            reader.next();

            if (reader.getEventType() == XMLStreamConstants.START_ELEMENT) {

                // no-op for "fields", but process each "field" entry
                String nodeName = reader.getName().getLocalPart();

                switch (nodeName) {
                    case "fields":
                        cdataIsBase64 = nodeAttributes(reader).anyMatch(MenuDataXmlParserHelper::isBase64EncodingAttribute);
                        break;
                    case "field":
                        readXmlFieldValue(reader, cdataIsBase64, loadMap);
                        break;
                    default:
                        LOGGER.warn("Unexpected xml01 start element: {}", nodeName);
                        break;
                }

            } else if (reader.getEventType() == XMLStreamConstants.END_ELEMENT) {
                // when we hit </fields>, we're done
                String nodeName = reader.getName().getLocalPart();
                if ("fields".equals(nodeName)) {
                    return;
                }
            }
        }
    }

    /**
     * Returns true if the attribute identifies the content as base64-encoded, otherwise false
     */
    private static boolean isBase64EncodingAttribute(Map.Entry<String, String> attr) {
        return CDATA_ENCODING_ATTRIB.equals(attr.getKey())
                && CDATA_ENCODING_BASE64.equals(attr.getValue());
    }

    /**
     * Generate a stream of Map.Entry containing each attribute name/value pair for the current node
     */
    @NotNull
    private static Stream<Map.Entry<String, String>> nodeAttributes(XMLStreamReader reader) {
        Function1<Integer, Map.Entry<String, String>> mapNameAndValue = Function2.of(MenuDataXmlParserHelper::nodeAttributeMapEntry).apply(reader);
        return IntStream.range(0, reader.getAttributeCount())
                .mapToObj(mapNameAndValue::apply);
    }

    /**
     * Constructs a Map.Entry for the attribute name/value pair at index idx
     */
    @NotNull
    private static Map.Entry<String, String> nodeAttributeMapEntry(XMLStreamReader reader, int idx) {
        return new AbstractMap.SimpleEntry<>(
                reader.getAttributeName(idx).getLocalPart(),
                reader.getAttributeValue(idx));
    }

    /**
     * Returns true if the attribute identifies a node name or type, otherwise false
     */
    private static boolean isNameOrTypeAttribute(Map.Entry<String, String> attr) {
        return "type".equals(attr.getKey()) || "name".equals(attr.getKey());
    }

    /**
     * From JavaDoc for XMLStreamReader.getElementText() -- because this
     * side-effect is confusing! Precondition: the current event is
     * START_ELEMENT. Postcondition: the current event is the corresponding
     * END_ELEMENT.
     *
     * @throws XMLStreamException
     */
    private static void readXmlFieldValue(XMLStreamReader reader, boolean cdataIsBase64, Map<String, Object> loadMap) throws XMLStreamException {
        checkState(reader.getEventType() == XMLStreamConstants.START_ELEMENT, "Current event type must be START_ELEMENT");

        Map<String, String> attrs = nodeAttributes(reader)
                .filter(MenuDataXmlParserHelper::isNameOrTypeAttribute)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        String name = attrs.getOrDefault("name", null);
        String type = attrs.getOrDefault("type", null);


        Object value;

        if ((type == null) || "null".equals(type)) {
            value = null;
        } else if ("String".equals(type)) {
            value = readCData(reader, cdataIsBase64);
        } else if ("Date".equals(type)) {
            value = new Date(Long.parseLong(readCData(reader, cdataIsBase64)));
        } else if ("ZonedDateTime".equals(type)) {
            value = ZonedDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(readCData(reader, cdataIsBase64))), ZoneId.systemDefault());
        } else if ("Long".equals(type)) {
            value = Long.parseLong(readCData(reader, cdataIsBase64));
        } else if ("Boolean".equals(type)) {
            value = Boolean.parseBoolean(readCData(reader, cdataIsBase64));
        } else if ("Double".equals(type)) {
            value = Double.parseDouble(readCData(reader, cdataIsBase64));
        } else if ("List<String>".equals(type)) {
            value = processSimpleList(reader, cdataIsBase64);
        } else if ("List<Long>".equals(type)) {
            value = processSimpleList(reader, cdataIsBase64, Long::parseLong);
        } else if ("List<Map<String,Object>>".equals(type)) {
            value = processMapList(reader);
        } else if ("Map<String,Object>".equals(type)) { // for nested DTOs
            value = processMap(reader);
        } else if ("MenuData".equals(type)) {
            LOGGER.warn("'MenuData' fields stored in xml01 not supported! field {}; value {}",
                    name, readCData(reader, cdataIsBase64));
            value = null;
        } else {
            throw new RuntimeException("Unknown Encrypted field type: " + type + " in field " + name);
        }

        loadMap.put(name, value);
    }

    @NotNull
    private static Map<String, Object> processMap(XMLStreamReader reader) throws XMLStreamException {
        Map<String, Object> nestedFieldMap = new HashMap<>();

        // read like when reading a top-level xml01
        readXmlFields(reader, nestedFieldMap);

        return nestedFieldMap;
    }

    @NotNull
    private static List<Map<String, Object>> processMapList(XMLStreamReader reader) throws XMLStreamException {

        List<Map<String, Object>> nestedDtoList = new ArrayList<>();
        while (reader.hasNext()) {
            reader.next();
            if (reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
                String nodeName = reader.getName().getLocalPart();
                if ("item".equals(nodeName)) {
                    nestedDtoList.add(processMap(reader));
                } else {
                    LOGGER.warn("Unexpected xml01 start element: {}", reader.getName().getLocalPart());
                }
            } else if (reader.getEventType() == XMLStreamConstants.END_ELEMENT) {
                // when we hit </field>, we're done
                String nodeName = reader.getName().getLocalPart();
                if ("field".equals(nodeName)) {
                    break;
                }
            }
        }
        return nestedDtoList;
    }

    @NotNull
    private static List<String> processSimpleList(XMLStreamReader reader, boolean cdataIsBase64) throws XMLStreamException {
        return processSimpleList(reader, cdataIsBase64, identity());
    }

    @NotNull
    private static <T> List<T> processSimpleList(XMLStreamReader reader, boolean cdataIsBase64, Function1<String, T> converter) throws XMLStreamException {

        List<T> values = new ArrayList<>();
        while (reader.hasNext()) {
            reader.next();
            if (reader.getEventType() == XMLStreamConstants.END_ELEMENT) {
                break;
            }
            if (reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
                values.add(converter.apply(readCData(reader, cdataIsBase64)));
            }
        }
        return values;
    }

    @NotNull
    private static String readCData(XMLStreamReader reader, boolean cdataIsBase64) throws XMLStreamException {
        String value = reader.getElementText();
        return cdataIsBase64 ? fromBase64(value) : value;
    }

    /**
     * Decodes a base64 string to a UTF8-encoded string
     */
    @NotNull
    private static String fromBase64(String value) {
        if (StringUtils.isEmpty(value)) {
            return value;
        }

        byte[] asBytes = Base64.getDecoder().decode(value);
        // utf-8 is always present, so suppress the annoying checked exception
        return unchecked(() -> new String(asBytes, "utf-8")).get();

    }
}
