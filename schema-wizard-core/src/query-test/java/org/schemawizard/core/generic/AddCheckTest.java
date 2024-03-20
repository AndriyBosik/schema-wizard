package org.schemawizard.core.generic;

import org.junit.jupiter.api.Test;
import org.schemawizard.core.config.GenericTest;
import org.schemawizard.core.migration.builder.operation.AddCheck;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.Operation;

public class AddCheckTest extends GenericTest {
    @Test
    public void shouldGenerateForTable() {
        Operation operation = AddCheck.builder("users")
                .name("chk_users_age_greater_than_zero")
                .condition("age > 0")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("add-check/table", info.getSql());
    }

    @Test
    public void shouldGenerateForTableAndSchema() {
        Operation operation = AddCheck.builder("schemawizard", "users")
                .name("chk_users_age_greater_than_zero")
                .condition("age > 0")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("add-check/schema-and-table", info.getSql());
    }
}
