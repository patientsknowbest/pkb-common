package com.pkb.common.config;

import java.util.function.Supplier;

@FunctionalInterface
interface ConfigLoader extends Supplier<RawConfigStorage> {

    RawConfigStorage load();

    @Override
    default RawConfigStorage get() {
        return load();
    }
}
