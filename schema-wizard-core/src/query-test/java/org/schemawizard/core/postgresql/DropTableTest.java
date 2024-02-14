package org.schemawizard.core.postgresql;

import org.junit.jupiter.api.Test;
import org.schemawizard.core.config.PostgreSqlTest;
import org.schemawizard.core.migration.builder.operation.DropTable;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.Operation;

public class DropTableTest extends PostgreSqlTest {
    @Test
    public void shouldDropTable() {
        Operation operation = DropTable.builder("example", "users").build();
        MigrationInfo info = operationResolverService.resolve(operation);

        assertQuery("drop-table", info.getSql());
    }
}
