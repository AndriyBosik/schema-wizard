package io.github.andriybosik.schemawizard.core.generic;

import org.junit.jupiter.api.Test;
import io.github.andriybosik.schemawizard.core.config.GenericTest;
import io.github.andriybosik.schemawizard.core.migration.builder.operation.DropConstraint;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationInfo;
import io.github.andriybosik.schemawizard.core.migration.operation.Operation;

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
