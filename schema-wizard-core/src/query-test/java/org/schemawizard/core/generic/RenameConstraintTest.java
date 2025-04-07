package org.schemawizard.core.generic;

import org.junit.jupiter.api.Test;
import org.schemawizard.core.config.GenericTest;
import org.schemawizard.core.config.extension.DisableFor;
import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.migration.builder.operation.RenameConstraint;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.Operation;

public class RenameConstraintTest extends GenericTest {
    @Test
    @DisableFor(DatabaseProvider.MYSQL)
    public void shouldGenerateRenameConstraintForTable() {
        Operation operation = RenameConstraint.builder("pk_users", "pk_users_id")
                .table("users")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("rename-constraint/table", info.getSql());
    }

    @Test
    @DisableFor(DatabaseProvider.MYSQL)
    public void shouldGenerateRenameConstraintForSchemaAndTable() {
        Operation operation = RenameConstraint.builder("pk_users", "pk_users_id")
                .schema("schemawizard")
                .table("users")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("rename-constraint/schema-and-table", info.getSql());
    }
}
