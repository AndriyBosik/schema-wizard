package org.schemawizard.core.config;

import org.schemawizard.core.metadata.DatabaseProvider;

public class MySqlTest extends GenericTest {
    protected MySqlTest() {
        super(DatabaseProvider.MYSQL);
    }
}
