package com.pkb.common;

import static com.pkb.common.CacheUtils.getUnclearableCaches;

import static java.util.Collections.singletonList;

import java.util.List;

import org.junit.Test;

import org.springframework.cache.annotation.Cacheable;

import com.github.karsaig.approvalcrest.MatcherAssert;
import com.github.karsaig.approvalcrest.matcher.Matchers;
import com.pkb.testing.gson.DefaultPKBGsonConfigurationFactory;

class NaughtyTestClass {
    @Cacheable("naughtyCache")
    public String getStuff() {
        return "No";
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

class IndifferentTestClass  {
    public String getStuff() {
        return "Maybe";
    }
}

public class CacheUtilsTest {

    @Test
    public void testGetUnclearableCaches() throws Exception {
        List<Class> actual = getUnclearableCaches();
        List<Class> wanted = singletonList(NaughtyTestClass.class);
        MatcherAssert
                .assertThat(actual, Matchers.sameBeanAs(wanted).withGsonConfiguration(DefaultPKBGsonConfigurationFactory.pkbConfigWithVavr()));
    }
}
