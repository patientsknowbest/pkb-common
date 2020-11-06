package com.pkb.common.testing;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.Tag;
import org.springframework.test.context.ActiveProfiles;

/**
 * This tag helps organise integration tests for spring boot applications.
 * When applied to a junit 5 test, it enables it to be filtered in maven
 * using "groups" in the surefire/failsafe configuration.
 *
 * In addition, it applies the spring profile "integration-test" allowing
 * selective configuration overrides.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("integration-test")
@Documented
@Tag("integration-test")
public @interface IntegrationTest {
}
