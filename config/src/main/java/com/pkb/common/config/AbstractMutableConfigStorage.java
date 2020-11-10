package com.pkb.common.config;

public abstract class AbstractMutableConfigStorage extends AbstractBaseConfigStorage {

    @Override
    public void setValue(String key, String value) {
        if (!MUTABLE_CONFIG_KEY.equals(key)) {
            setValueInternal(key, value);
        }
    }

    abstract void setValueInternal(String key, String value);

}
