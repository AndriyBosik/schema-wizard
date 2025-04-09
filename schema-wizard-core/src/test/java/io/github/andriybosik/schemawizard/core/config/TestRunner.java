package io.github.andriybosik.schemawizard.core.config;

public interface TestRunner {
    void configureEnvironment();

    void shutdownEnvironment();
}
