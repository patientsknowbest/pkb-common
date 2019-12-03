package com.pkb.common.ehrdata;

import static com.github.karsaig.approvalcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.Test;

public class NoOpAutocloseableTimerTest {

    @Test
    public void startTimer_returnsSelf() {
        NoOpAutocloseableTimer noOpAutocloseableTimer = new NoOpAutocloseableTimer();
        AutocloseableTimer autocloseableTimer = noOpAutocloseableTimer.startTimer();

        assertThat(autocloseableTimer, is(noOpAutocloseableTimer));
    }

    @Test
    public void observeDuration_returnsZero() {
        NoOpAutocloseableTimer noOpAutocloseableTimer = new NoOpAutocloseableTimer();
        double v = noOpAutocloseableTimer.observeDuration();

        assertThat(v, is(0.0));
    }

    @Test
    public void close_doesNothing() {
        NoOpAutocloseableTimer noOpAutocloseableTimer = new NoOpAutocloseableTimer();
        noOpAutocloseableTimer.close();
    }
}