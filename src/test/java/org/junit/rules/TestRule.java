package org.junit.rules;

/**
 * This is required by Testcontainers but actually unused.
 * By defining this here, we can avoid loading Junit-4.
 * See: https://github.com/testcontainers/testcontainers-java/issues/970
 */
@SuppressWarnings("unused")
public interface TestRule {
}
