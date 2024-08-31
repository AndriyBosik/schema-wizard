package org.schemawizard.core.config.impl;

import org.schemawizard.core.config.DbInitializer;
import org.schemawizard.core.config.TestRunner;
import org.schemawizard.core.utils.EnvUtils;

public class PipelineTestRunner implements TestRunner {
    private final DbInitializer dbInitializer;

    public PipelineTestRunner() {
        dbInitializer = newInstance();
    }

    @Override
    public void configureEnvironment() {
        dbInitializer.init();
    }

    private DbInitializer newInstance() {
        switch (EnvUtils.PROVIDER) {
            case POSTGRESQL:
                return new PostgreSqlDbInitializer();
            case ORACLE:
                return new OracleDbInitializer();
        }
        throw new IllegalArgumentException("No initializer found for database provider " + EnvUtils.PROVIDER);
    }

    @Override
    public void shutdownEnvironment() {
        dbInitializer.shutdown();
    }
}
