package io.github.andriybosik.schemawizard.core.generic;

import org.junit.jupiter.api.Test;
import io.github.andriybosik.schemawizard.core.config.GenericTest;
import io.github.andriybosik.schemawizard.core.config.extension.DisableFor;
import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;
import io.github.andriybosik.schemawizard.core.migration.builder.operation.RenameConstraint;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationInfo;
import io.github.andriybosik.schemawizard.core.migration.operation.Operation;

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
