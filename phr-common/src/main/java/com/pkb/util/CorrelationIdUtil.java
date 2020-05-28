package com.pkb.util;

import static com.pkb.util.Constants.NHS_LOGIN_CORRELATION_ID;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.google.common.annotations.VisibleForTesting;

import io.vavr.control.Try;

public class CorrelationIdUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());
    private static final String CORRELATION_ID = "correlationId";

    public String get() {
        return MDC.get(CORRELATION_ID);
    }

    public void putIfAbsent(UUID value) {
        putIfAbsent(value.toString());
    }

    public void putIfAbsent(String value) {
        if (get() == null) {
            put(value);
        }
    }

    public void put(UUID value) {
        put(value.toString());
    }

    public void put(String value) {
        MDC.put(CORRELATION_ID, value);
    }

    public void remove() {
        MDC.remove(CORRELATION_ID);
    }

    public UUID getCorrelationIdFromRequest(HttpServletRequest request) {
        return getRequestId(request).orElse(getAlternativeUUID());
    }

    @VisibleForTesting
    UUID getAlternativeUUID() {
        return randomUUID();
    }

    private Optional<UUID> getRequestId(HttpServletRequest request) {
        String requestId = (String) request.getSession().getAttribute(NHS_LOGIN_CORRELATION_ID);
        if (isNotBlank(requestId)) {
            return Try.of(() -> UUID.fromString(requestId))
                    .onFailure(t -> LOGGER.warn("Non-UUID request id received in header: {}", requestId))
                    .toJavaOptional();
        }

        return Optional.empty();
    }
}
