package io.pkb.logcapture;

import ch.qos.logback.classic.Level;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * Utility for verifying logs in tests.
 * Example usage: At the top of your class, register the extension:
 * 
 *     @RegisterExtension
 *     LogCaptureExtension logCaptureExtension = LogCaptureExtension.create().forLevel(Level.OFF).forType(MyClass.class);
 * 
 * Then, during tests you can verify log statements like so:
 * 
 *         assertTrue(logCaptureExtension.contains(msg));
 *         assertTrue(logCaptureExtension.isStatementAtLevel(msg, Level.ERROR));
 *         assertEquals(1, logCaptureExtension.size());
 */
@SuppressWarnings("unused")
public class LogCaptureExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private final LogCapture logCapture = LogCapture.create();

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
        logCapture.reset();
    }

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) throws Exception {
        logCapture.prepare();
    }

    public static LogCaptureExtension create() {
        return new LogCaptureExtension();
    }

    public LogCaptureExtension forLevel(Level level) {
        logCapture.forLevel(level);
        return this;
    }

    @SuppressWarnings("SameParameterValue")
    public LogCaptureExtension forType(Class<?> type) {
        logCapture.forType(type);
        return this;
    }

    public boolean contains(String statement) {
        return logCapture.contains(statement);
    }

    @SuppressWarnings("SameParameterValue")
    public boolean isStatementAtLevel(String statement, Level level) {
        return logCapture.isStatementAtLevel(statement, level);
    }

    public int size() {
        return logCapture.size();
    }
}
