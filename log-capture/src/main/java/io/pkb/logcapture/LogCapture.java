package io.pkb.logcapture;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.slf4j.LoggerFactory;

import java.util.Vector;

public class LogCapture {

    private final LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
    private final Vector<Class<?>> sources = new Vector<>();
    private final ListAppender<ILoggingEvent> appender = new ListAppender<>();
    private Level level = Level.ALL;

    private LogCapture() {
        // NoOp
    }

    public static LogCapture create() {
        return new LogCapture();
    }

    void forLevel(Level level) {
        this.level = level;
        reset();
        prepare();
    }

    void forType(Class<?> type) {
        sources.add(type);
    }

    public boolean contains(String statement) {
        return appender.list.stream().anyMatch(event -> event.getFormattedMessage().contains(statement));
    }

    boolean isStatementAtLevel(String statement, Level level) {
        return appender.list.stream().filter(event -> event.getFormattedMessage().contains(statement)).allMatch(event -> event.getLevel().equals(level));
    }

    public int size() {
        return appender.list.size();
    }

    void prepare() {
        resetContext();
        addAppenderToSources();
        appender.start();
    }

    void reset() {
        appender.stop();
        resetContext();
    }

    private void addAppenderToSources() {
        sources.forEach(this::addAppender);
    }

    private void addAppender(Class<?> type) {
        Logger logger = (Logger) LoggerFactory.getLogger(type);
        logger.addAppender(appender);
        logger.setLevel(level);
    }

    private void resetContext() {
        context.reset();
    }
}
