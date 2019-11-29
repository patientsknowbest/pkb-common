package com.pkb.common.ehrdata;

public class NoOpAutocloseableTimer implements AutocloseableTimer {

    public static final NoOpAutocloseableTimer NO_OP_TIMER = new NoOpAutocloseableTimer();

    @Override
    public AutocloseableTimer startTimer() {
        return this;
    }

    @Override
    public double observeDuration() {
        return 0;
    }

    @Override
    public void close(){
    }
}
