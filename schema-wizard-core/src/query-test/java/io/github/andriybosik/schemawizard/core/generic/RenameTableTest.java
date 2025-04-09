package io.github.andriybosik.schemawizard.core.generic;

import org.junit.jupiter.api.Test;
import io.github.andriybosik.schemawizard.core.config.GenericTest;
import io.github.andriybosik.schemawizard.core.migration.builder.operation.RenameTable;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationInfo;
import io.github.andriybosik.schemawizard.core.migration.operation.Operation;

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
