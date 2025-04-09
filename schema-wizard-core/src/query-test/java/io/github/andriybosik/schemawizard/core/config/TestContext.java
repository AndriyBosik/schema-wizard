package io.github.andriybosik.schemawizard.core.config;

import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;
import io.github.andriybosik.schemawizard.core.utils.TestStringUtils;

public class TestContext {
    private final static DatabaseProvider provider;

    static {
        String envProvider = System.getenv("PROVIDER");
        if (TestStringUtils.isBlank(envProvider)) {
            throw new IllegalArgumentException("Database provider was not provided");
        }
        provider = DatabaseProvider.valueOf(envProvider);
    }

    public static DatabaseProvider getProvider() {
        return provider;
    }
}
