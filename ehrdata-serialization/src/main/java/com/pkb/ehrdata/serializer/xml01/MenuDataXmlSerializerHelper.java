package com.pkb.ehrdata.serializer.xml01;

import static io.vavr.API.unchecked;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import io.prometheus.client.Summary;

public class MenuDataXmlSerializerHelper {

    private static final String CDATA_ENCODING_ATTRIB = "cdataEncoding";
    private static final String CDATA_ENCODING_BASE64 = "Base64";

    private static final Summary XML_CREATE_SECONDS = Summary.build()
            .name("pkb_phr_menudata_xmlcreateseconds")
            .help("Time needed to serialize fields to xml01")
            .quantile(0.5, 0.05)
            .quantile(0.9, 0.01)
            .quantile(0.95, 0.005)
            .quantile(0.99, 0.001)
            .register();

    private MenuDataXmlSerializerHelper() {
    }

    @NotNull
    public static byte[] marshalEncryptedFields(@NotNull Map<String, Object> encryptedFields) {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();

        return unchecked(() -> {
            XMLStreamWriter writer = null;
            try (
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    Summary.Timer ignored = XML_CREATE_SECONDS.startTimer()
            ) {

                writer = factory.createXMLStreamWriter(baos, "UTF-8");
                writer.writeStartDocument("UTF-8", "1.0");
                writeXmlFields(writer, encryptedFields);
                writer.writeEndDocument();

                return baos.toByteArray();
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }).get();
    }

    private static void writeXmlFields(@NotNull XMLStreamWriter writer, @NotNull Map<String, Object> fieldMap) throws XMLStreamException {
        writer.writeStartElement("fields");
        writer.writeAttribute(CDATA_ENCODING_ATTRIB, CDATA_ENCODING_BASE64);

        if (fieldMap != null) {
            for (Map.Entry<String, Object> entry : fieldMap.entrySet()) {
                writer.writeStartElement("field");
                writer.writeAttribute("name", entry.getKey());
                writeXmlFieldValue(writer, entry.getValue());
                writer.writeEndElement();
            }
        }
        writer.writeEndElement();
    }

    private static void writeXmlFieldValue(@NotNull XMLStreamWriter writer, @NotNull Object value) throws XMLStreamException {
        if ((value == null)
                || ((value instanceof String) && ((String) value).isEmpty())
                || ((value instanceof Collection<?>) && ((Collection) value).isEmpty())) {
            writer.writeAttribute("type", "null");
            return;
        } else if (value instanceof String) {
            writer.writeAttribute("type", "String");
            writer.writeCData(toBase64(value.toString()));
            return;
        } else if (value instanceof Date) {
            writer.writeAttribute("type", "Date");
            writer.writeCData(toBase64(Long.toString(((Date) value).getTime())));
            return;
        } else if (value instanceof Long) {
            writer.writeAttribute("type", "Long");
            writer.writeCData(toBase64(value.toString()));
            return;
        } else if (value instanceof Boolean) {
            writer.writeAttribute("type", "Boolean");
            writer.writeCData(toBase64(value.toString()));
            return;
        } else if (value instanceof Double) {
            writer.writeAttribute("type", "Double");
            writer.writeCData(toBase64(value.toString()));
            return;
        } else if (value instanceof List<?>) {
            Object firstItem = ((List) value).get(0);
            if (firstItem instanceof String) {
                writer.writeAttribute("type", "List<String>");
                for (String listItem : ((List<String>) value)) {
                    writer.writeStartElement("item");
                    writer.writeCData(toBase64(listItem));
                    writer.writeEndElement();
                }
            } else if (firstItem instanceof Long) {
                writer.writeAttribute("type", "List<Long>");
                for (Long listItem : ((List<Long>) value)) {
                    writer.writeStartElement("item");
                    writer.writeCData(toBase64(listItem.toString()));
                    writer.writeEndElement();
                }
            } else if (firstItem instanceof Enum) {
                writer.writeAttribute("type", "List<String>");
                for (Enum listItem : ((List<Enum>) value)) {
                    writer.writeStartElement("item");
                    writer.writeCData(toBase64(listItem.toString()));
                    writer.writeEndElement();
                }
            } else if (firstItem instanceof Map) {
                // list of nested DTOs!
                writer.writeAttribute("type", "List<Map<String,Object>>");
                for (Map<String, Object> listItem : ((List<Map<String, Object>>) value)) {
                    writer.writeStartElement("item");
                    // add in the nested fields just as we add in these...
                    writeXmlFields(writer, listItem);
                    writer.writeEndElement(); // item
                }
            } else {
                throw new RuntimeException(value.getClass() + "<" + firstItem.getClass() + "> is not a recognized encrypted field type");
            }
        } else if (value instanceof Map) {
            Map.Entry firstEntry = (Map.Entry) ((Map) value).entrySet().iterator().next();
            if (!(firstEntry.getKey() instanceof String)) {
                throw new RuntimeException(value.getClass() + "<" + firstEntry.getKey().getClass()
                        + ", ?> is not a recognized encrypted field type -- only Map<String, Object> for nested DTOs");
            }

            writer.writeAttribute("type", "Map<String,Object>");
            // add in the nested fields just as we add in these...
            writeXmlFields(writer, (Map<String, Object>) value);
        } else {
            throw new RuntimeException(value.getClass() + " is not a recognized encrypted field type");
        }
    }

    @NotNull
    private static String toBase64(String s) {
        if (StringUtils.isEmpty(s)) {
            return s;
        }

        // utf-8 is always present, so suppress the annoying checked exception
        return unchecked(() -> Base64.getEncoder().encodeToString(s.getBytes("utf-8"))).get();
    }
}
