package com.pkb.common.ehrdata.serializer.xml01;

import static io.vavr.API.unchecked;

import java.io.ByteArrayOutputStream;
import java.time.ZonedDateTime;
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
import org.jetbrains.annotations.Nullable;

import com.pkb.common.ehrdata.AutocloseableTimer;

public class MenuDataXmlSerializerHelper {

    private static final String CDATA_ENCODING_ATTRIB = "cdataEncoding";
    private static final String CDATA_ENCODING_BASE64 = "Base64";

    private MenuDataXmlSerializerHelper() {
    }

    @NotNull
    public static byte[] marshalEncryptedFields(@NotNull Map<String, Object> encryptedFields, AutocloseableTimer xmlCreationTimer) {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();

        return unchecked(() -> {
            XMLStreamWriter writer = null;
            try (
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    AutocloseableTimer timer = xmlCreationTimer.startTimer()
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

    private static void writeXmlFieldValue(@NotNull XMLStreamWriter writer, @Nullable Object value) throws XMLStreamException {
        if ((value == null)
                || ((value instanceof String) && ((String) value).isEmpty())
                || ((value instanceof Collection<?>) && ((Collection) value).isEmpty())) {
            writer.writeAttribute("type", "null");
        } else if (value instanceof String) {
            writer.writeAttribute("type", "String");
            writer.writeCData(toBase64(value.toString()));
        } else if (value instanceof Date) {
            writer.writeAttribute("type", "Date");
            writer.writeCData(toBase64(Long.toString(((Date) value).getTime())));
        } else if (value instanceof ZonedDateTime) {
            writer.writeAttribute("type", "ZonedDateTime");
            writer.writeCData(toBase64(Long.toString(((ZonedDateTime) value).toInstant().toEpochMilli())));
        } else if (value instanceof Long) {
            writer.writeAttribute("type", "Long");
            writer.writeCData(toBase64(value.toString()));
        } else if (value instanceof Boolean) {
            writer.writeAttribute("type", "Boolean");
            writer.writeCData(toBase64(value.toString()));
        } else if (value instanceof Double) {
            writer.writeAttribute("type", "Double");
            writer.writeCData(toBase64(value.toString()));
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
