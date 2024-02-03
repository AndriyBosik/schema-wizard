package org.schemawizard.core.config;

import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.utils.StringUtils;

public class TestContext {
    private final static DatabaseProvider provider;

    static {
        String envProvider = System.getenv("PROVIDER");
        if (StringUtils.isBlank(envProvider)) {
            throw new IllegalArgumentException("Database provider was not provided");
        }
        provider = DatabaseProvider.valueOf(envProvider);
    }

    public static DatabaseProvider getProvider() {
        return provider;
    }
}
