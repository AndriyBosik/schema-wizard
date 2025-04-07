package org.schemawizard.core.config.impl;

import org.schemawizard.core.config.AbstractDbInitializer;
import org.schemawizard.core.config.DbInitializer;
import org.schemawizard.core.utils.EnvUtils;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgreSqlDbInitializer extends AbstractDbInitializer {
    public PostgreSqlDbInitializer() {
        super(
                new PostgreSQLContainer<>("postgres:16-alpine")
                        .withDatabaseName(EnvUtils.DB_DATABASE)
                        .withExposedPorts(PostgreSQLContainer.POSTGRESQL_PORT)
                        .withUsername(EnvUtils.DB_USERNAME)
                        .withPassword(EnvUtils.DB_PASSWORD),
                PostgreSQLContainer.POSTGRESQL_PORT);
    }
}
