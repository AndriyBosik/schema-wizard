package io.github.andriybosik.schemawizard.core.config;

import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;

public class MySqlTest extends GenericTest {
    protected MySqlTest() {
        super(DatabaseProvider.MYSQL);
    }
}
