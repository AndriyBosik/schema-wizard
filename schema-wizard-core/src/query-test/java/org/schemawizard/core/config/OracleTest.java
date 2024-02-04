package org.schemawizard.core.config;

import org.schemawizard.core.metadata.DatabaseProvider;

public class OracleTest extends GenericTest {
    protected OracleTest() {
        super(DatabaseProvider.ORACLE);
    }
}
