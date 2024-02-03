package org.schemawizard.core.config;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.schemawizard.core.metadata.DatabaseProvider;

public class OracleTest extends GenericTest {

    protected OracleTest() {
        super(DatabaseProvider.ORACLE);
    }

    @BeforeEach
    public void beforeEach() {
        Assumptions.assumeFalse(TestContext.getProvider() != this.provider);
    }
}
