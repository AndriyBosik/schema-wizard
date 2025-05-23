package io.github.andriybosik.schemawizard.core.config.impl;

import io.github.andriybosik.schemawizard.core.config.AbstractDbInitializer;
import io.github.andriybosik.schemawizard.core.utils.EnvUtils;
import org.testcontainers.containers.MSSQLServerContainer;

public class SqlServerDbInitializer extends AbstractDbInitializer {
    public SqlServerDbInitializer() {
        super(
                new MSSQLServerContainer<>("mcr.microsoft.com/mssql/server:2025-latest")
                        .acceptLicense()
                        .withInitScript("init-script/sqlserver.sql")
                        .withExposedPorts(MSSQLServerContainer.MS_SQL_SERVER_PORT)
                        .withPassword(EnvUtils.DB_PASSWORD),
                MSSQLServerContainer.MS_SQL_SERVER_PORT);
    }
}
