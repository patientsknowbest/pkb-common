package com.pkb.common.config;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LayeredLoader {

    public static final String PKB_SERVICE_CONFIG = "PKB_SERVICE_CONFIG";

    public static final String DEFAULT_CONFIG_FILE_PATH = "config/default";

    public static final LayeredLoader defaultLoader() {
        return new LayeredLoader(System.getenv(PKB_SERVICE_CONFIG));
    }

    private List<String> propertyFiles;

    public LayeredLoader(String configFileList) {
        if (configFileList == null) {
            propertyFiles = singletonList(DEFAULT_CONFIG_FILE_PATH);
        } else {
            String[] filePaths = configFileList.split(":");
            List<String> propertyFiles = new ArrayList<>(filePaths.length + 1);
            propertyFiles.add("config/default");
            propertyFiles.addAll(asList(filePaths));
            this.propertyFiles = unmodifiableList(propertyFiles);
        }
    }

    public RawConfigStorage load() {
        return propertyFiles.stream()
                .map(filePath -> filePath + ".properties")
                .map(this::attemptToLoad)
                .filter(Objects::nonNull)
                .reduce(RawConfigStorage.EMPTY, RawConfigStorage::merge);
    }

    private RawConfigStorage attemptToLoad(String propFilePath) {
        try {
            return new PropertyFileBasedLoader().load(propFilePath);
        } catch (ConfigurationException e) {
            return null;
        }
    }

}
