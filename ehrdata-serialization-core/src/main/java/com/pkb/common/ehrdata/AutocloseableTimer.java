package com.pkb.common.ehrdata;

import java.io.Closeable;

public interface AutocloseableTimer extends Closeable {

    AutocloseableTimer startTimer();

    double observeDuration();
}
