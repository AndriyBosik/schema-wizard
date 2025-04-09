package io.github.andriybosik.schemawizard.core.config;

import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;

public class OracleTest extends GenericTest {
    protected OracleTest() {
        super(DatabaseProvider.ORACLE);
    }
}
