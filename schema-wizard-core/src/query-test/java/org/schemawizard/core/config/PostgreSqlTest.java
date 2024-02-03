package org.schemawizard.core.config;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.schemawizard.core.metadata.DatabaseProvider;

public class PostgreSqlTest extends GenericTest {

    protected PostgreSqlTest() {
        super(DatabaseProvider.POSTGRESQL);
    }

    @BeforeEach
    public void beforeEach() {
        Assumptions.assumeFalse(TestContext.getProvider() != this.provider);
    }
}
