package com.pkb.common.config;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

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
        PropertyFileBasedLoader propFileLoader = new PropertyFileBasedLoader();
        return propertyFiles.stream()
                .map(filePath -> filePath + ".properties")
                .map(propFileLoader::load)
                .reduce(RawConfigStorage.EMPTY, RawConfigStorage::merge);
    }

}
