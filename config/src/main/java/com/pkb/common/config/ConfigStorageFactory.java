package com.pkb.common.config;

public final class ConfigStorageFactory {

    private ConfigStorageFactory() {
    }

    public static ConfigStorage getConfigStorage() {
        return getConfigStorage(ImmutableRawConfigStorage.createDefault());
    }


    public static ConfigStorage getConfigStorage(ImmutableConfigStorage baseStorage) {
        if (baseStorage.isMutableConfigEnabled()) {
            return new MutableRawConfigStorage(baseStorage);
        }
        return baseStorage;
    }
}
