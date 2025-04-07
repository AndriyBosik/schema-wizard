package org.schemawizard.core.config.impl;

import org.schemawizard.core.config.AbstractDbInitializer;
import org.schemawizard.core.utils.EnvUtils;
import org.testcontainers.containers.MySQLContainer;

public class MySqlDbInitializer extends AbstractDbInitializer {
    public MySqlDbInitializer() {
        super(
                new MySQLContainer<>("mysql:8.0.32")
                        .withDatabaseName(EnvUtils.DB_DATABASE)
                        .withExposedPorts(MySQLContainer.MYSQL_PORT)
                        .withUsername(EnvUtils.DB_USERNAME)
                        .withPassword(EnvUtils.DB_PASSWORD),
                MySQLContainer.MYSQL_PORT);
    }
}
