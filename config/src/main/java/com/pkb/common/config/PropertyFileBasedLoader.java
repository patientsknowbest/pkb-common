package com.pkb.common.config;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyFileBasedLoader {

    public RawConfigStorage load(String propFilePath) {
        Properties props = new Properties();
        URL resource = Thread.currentThread().getContextClassLoader().getResource(propFilePath);
        try (InputStream stream = requireNonNull(resource, "resource " + propFilePath + " not found").openStream()){
            props.load(stream);
            Map<String, String> storage = new HashMap<>(props.size());
            props.forEach((k, v) -> storage.put((String) k, (String) v));
            return new RawConfigStorage(storage);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
