package com.pkb.common;

import static com.pkb.common.CacheUtils.getUnclearableCaches;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import com.github.karsaig.approvalcrest.MatcherAssert;
import com.github.karsaig.approvalcrest.matcher.Matchers;

import com.pkb.testing.gson.DefaultPKBGsonConfigurationFactory;

class NaughtyTestClass1 {
    @Cacheable("naughtyCache")
    public String getStuff() {
        return "No";
    }
}

class NaughtyTestClass2 {
    @CachePut("naughtyCache")
    public void putStuff(String stuff) {
    }
}

class NaughtyTestClass3 {
    @CacheEvict("naughtyCache")
    public void evictStuff(String stuff) {
    }
}

class NiceTestClass implements ClearableInternalState {
    @Override
    public void clearState() {
    }

    @Cacheable("niceCache")
    public String getStuff() {
        return "Yes";
    }
}

class IndifferentTestClass {
    public String getStuff() {
        return "Maybe";
    }
}

public class CacheUtilsTest {

    @Test
    public void testGetUnclearableCaches() throws Exception {
        List<Class> actual = getUnclearableCaches();
        actual.sort(Comparator.comparing(Class::getName));
        List<Class> wanted = Arrays.asList(NaughtyTestClass1.class, NaughtyTestClass2.class, NaughtyTestClass3.class);
        MatcherAssert
                .assertThat(actual, Matchers.sameBeanAs(wanted).withGsonConfiguration(DefaultPKBGsonConfigurationFactory.pkbConfigWithVavr()));
    }
}
