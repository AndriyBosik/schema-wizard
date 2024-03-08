package org.schemawizard.core.generic;

import org.junit.jupiter.api.Test;
import org.schemawizard.core.config.GenericTest;
import org.schemawizard.core.migration.builder.operation.RenameColumn;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.Operation;

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
}
