package org.schemawizard.core.config;

public interface DbInitializer {
    void init();

    void shutdown();
}
