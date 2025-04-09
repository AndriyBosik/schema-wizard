package io.github.andriybosik.schemawizard.core.config;

public interface DbInitializer {
    void init();

    void shutdown();
}
