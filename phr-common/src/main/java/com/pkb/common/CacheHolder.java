package com.pkb.common;

import static org.slf4j.LoggerFactory.getLogger;

import java.lang.invoke.MethodHandles;
import java.util.Collection;
import java.util.LinkedList;

import org.slf4j.Logger;

import com.google.common.cache.Cache;

public class CacheHolder {

    private static final Logger LOGGER = getLogger(MethodHandles.lookup().lookupClass());
    private static final Collection<Cache<?, ?>> LOGIN_CACHES = new LinkedList<>();

    public static <T, U> Cache<T, U> registerCache(Cache<T, U> cache) {
        LOGIN_CACHES.add(cache);
        return cache;
    }

    public static int getSize() {
        return LOGIN_CACHES.size();
    }

    public static void clearState() {
        LOGIN_CACHES.forEach(Cache::invalidateAll);
        LOGGER.info("Cleared internal states");
    }
}
