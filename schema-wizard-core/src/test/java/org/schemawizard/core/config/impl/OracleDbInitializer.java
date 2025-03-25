package org.schemawizard.core.config.impl;

import org.schemawizard.core.config.AbstractDbInitializer;
import org.schemawizard.core.utils.EnvUtils;
import org.testcontainers.oracle.OracleContainer;

public class OracleDbInitializer extends AbstractDbInitializer {
    private final static int ORACLE_PORT = 1521;

    public OracleDbInitializer() {
        super(
                new OracleContainer("gvenzl/oracle-free:23.5-slim-faststart")
                        .withDatabaseName(EnvUtils.DB_DATABASE)
                        .withExposedPorts(ORACLE_PORT)
                        .withUsername(EnvUtils.ORACLE_DB_APP_USERNAME)
                        .withPassword(EnvUtils.DB_PASSWORD),
                ORACLE_PORT);
    }
}
