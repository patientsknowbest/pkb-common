package com.pkb.common;

import static java.util.stream.Collectors.toList;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import com.google.common.reflect.ClassPath;

public class CacheUtils {

    public static List<Class> getUnclearableCaches() throws Exception {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        return ClassPath.from(cl).getTopLevelClassesRecursive("com.pkb").stream()
                .map(ClassPath.ClassInfo::load)
                .filter(c -> hasCacheableMethods(c) && !ClearableInternalState.class.isAssignableFrom(c))
                .collect(toList());
    }

    private static boolean hasCacheableMethods(Class clazz) {
        for (Method m : clazz.getDeclaredMethods()) {
            if (m.getAnnotation(Cacheable.class) != null || m.getAnnotation(CachePut.class) != null || m.getAnnotation(CacheEvict.class) != null) {
                return true;
            }
        }
        return false;
    }
}