package org.schemawizard.core.generic;

import org.junit.jupiter.api.Test;
import org.schemawizard.core.config.GenericTest;
import org.schemawizard.core.migration.builder.operation.DropTable;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.Operation;

public class DropTableTest extends GenericTest {
    @Test
    public void shouldGenerateForTable() {
        Operation operation = DropTable.builder("users")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("drop-table/table", info.getSql());
    }

    @Test
    public void shouldGenerateForSchemaAndTable() {
        Operation operation = DropTable.builder("schemawizard", "users")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("drop-table/schema-and-table", info.getSql());
    }

    @Test
    public void shouldGenerateDropTableIfExists() {
        Operation operation = DropTable.builder("users")
                .ifExists()
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("drop-table/if-exists", info.getSql());
    }
}
