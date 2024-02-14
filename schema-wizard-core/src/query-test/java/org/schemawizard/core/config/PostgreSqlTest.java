package org.schemawizard.core.config;

import org.schemawizard.core.metadata.DatabaseProvider;

public class PostgreSqlTest extends GenericTest {
    protected PostgreSqlTest() {
        super(DatabaseProvider.POSTGRESQL);
    }
}
