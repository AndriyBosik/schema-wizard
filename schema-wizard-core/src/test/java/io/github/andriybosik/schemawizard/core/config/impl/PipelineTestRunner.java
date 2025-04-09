package io.github.andriybosik.schemawizard.core.config.impl;

import io.github.andriybosik.schemawizard.core.config.DbInitializer;
import io.github.andriybosik.schemawizard.core.config.TestRunner;
import io.github.andriybosik.schemawizard.core.utils.EnvUtils;

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
            case MYSQL:
                return new MySqlDbInitializer();
        }
        throw new IllegalArgumentException("No initializer found for database provider " + EnvUtils.PROVIDER);
    }

    @Override
    public void shutdownEnvironment() {
    }
}
