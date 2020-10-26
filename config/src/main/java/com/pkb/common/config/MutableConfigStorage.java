package com.pkb.common.config;

public interface MutableConfigStorage extends ConfigStorage {

    @Override
    default boolean isMutableConfigEnabled() {
        return true;
    }

}
