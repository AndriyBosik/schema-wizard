package org.schemawizard.core.generic;

import org.junit.jupiter.api.Test;
import org.schemawizard.core.config.GenericTest;
import org.schemawizard.core.migration.builder.operation.RenameTable;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.Operation;

public class RenameTableTest extends GenericTest {
    @Test
    public void shouldGenerateRenameTableForTable() {
        Operation operation = RenameTable.builder("users")
                .newName("customers")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("rename-table/table", info.getSql());
    }

    @Test
    public void shouldGenerateRenameTableForSchemaAndTable() {
        Operation operation = RenameTable.builder("schemawizard", "users")
                .newName("customers")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("rename-table/schema-and-table", info.getSql());
    }
}
