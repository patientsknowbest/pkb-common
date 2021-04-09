package com.pkb.util;

import com.google.common.annotations.VisibleForTesting;
import io.vavr.control.Try;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;


import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class CorrelationIdUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());
    private static final String CORRELATION_ID = "correlationId";

    public static final String NHS_LOGIN_CORRELATION_ID = "nhs.login.correlation.id";

    public @Nullable UUID get() {
        return maybeGet().orElse(null);
    }

    public @Nullable String getAsString() {
        return maybeGet().map(UUID::toString).orElse(null);
    }

    public Optional<UUID> maybeGet() {
        return Optional.ofNullable(StringUtils.trimToNull(MDC.get(CORRELATION_ID))).map(UUID::fromString);
    }

    public void putIfAbsent(UUID value) {
        if (get() == null) {
            put(value);
        }
    }

    public void put(UUID value) {
        MDC.put(CORRELATION_ID, value.toString());
    }

    public void remove() {
        MDC.remove(CORRELATION_ID);
    }

    public UUID getCorrelationIdFromRequest(HttpServletRequest request) {
        return getRequestIdFromHeader(request)
                .or(() -> getRequestIdFromNhsSession(request))
                .orElseGet(this::getAlternativeUUID);
    }

    public UUID getCorrelationIdFromStringOrGenerateNewOne(String requestId) {
        return parseRequestId(requestId).orElse(getAlternativeUUID());
    }

    @VisibleForTesting
    UUID getAlternativeUUID() {
        return randomUUID();
    }

    private Optional<UUID> getRequestIdFromNhsSession(HttpServletRequest request) {
        String requestId = (String) request.getSession().getAttribute(NHS_LOGIN_CORRELATION_ID);
        return parseRequestId(requestId);
    }

    private Optional<UUID> getRequestIdFromHeader(HttpServletRequest request) {
        String requestId = request.getHeader("X-Request-ID");
        return parseRequestId(requestId);
    }

    private Optional<UUID> parseRequestId(String requestId) {
        if (isNotBlank(requestId)) {
            return Try.of(() -> UUID.fromString(requestId))
                      .onFailure(t -> LOGGER.warn("Non-UUID request id received in header: {}", requestId))
                      .toJavaOptional();
        }
        return Optional.empty();
    }
}
