package io.github.andriybosik.schemawizard.core.config;

import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;

public class PostgreSqlTest extends GenericTest {
    protected PostgreSqlTest() {
        super(DatabaseProvider.POSTGRESQL);
    }
}
