package org.schemawizard.core.generic;

import org.junit.jupiter.api.Test;
import org.schemawizard.core.config.GenericTest;
import org.schemawizard.core.migration.builder.operation.DropConstraint;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.Operation;

public class DropConstraintTest extends GenericTest {
    @Test
    public void shouldGenerateDropConstraintForTable() {
        Operation operation = DropConstraint.builder("users")
                .name("con_generic")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("drop-constraint/table", info.getSql());
    }

    @Test
    public void shouldGenerateDropConstraintForSchemaAndTable() {
        Operation operation = DropConstraint.builder("schemawizard", "users")
                .name("con_generic")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("drop-constraint/schema-and-table", info.getSql());
    }

    @Test
    public void shouldGenerateDropConstraintIfExists() {
        Operation operation = DropConstraint.builder("users")
                .name("con_generic")
                .ifExists()
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("drop-constraint/if-exists", info.getSql());
    }
}
