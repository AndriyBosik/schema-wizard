package io.github.andriybosik.schemawizard.core.generic;

import org.junit.jupiter.api.Test;
import io.github.andriybosik.schemawizard.core.config.GenericTest;
import io.github.andriybosik.schemawizard.core.migration.builder.operation.RenameColumn;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationInfo;
import io.github.andriybosik.schemawizard.core.migration.operation.Operation;

public class RenameColumnTest extends GenericTest {
    @Test
    public void shouldGenerateRenameColumnForTable() {
        Operation operation = RenameColumn.builder("name", "first_name")
                .table("users")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("rename-column/table", info.getSql());
    }

    @Test
    public void shouldGenerateRenameColumnForSchemaAndTable() {
        Operation operation = RenameColumn.builder("name", "first_name")
                .schema("schemawizard")
                .table("users")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("rename-column/schema-and-table", info.getSql());
    }

    @Test
    public void shouldMapCamelCaseColumnsToSnakeCase() {
        Operation operation = RenameColumn.builder("firstName", "lastName")
                .schema("schemawizard")
                .table("users")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("rename-column/map-camel-case-to-snake-case", info.getSql());
    }
}
