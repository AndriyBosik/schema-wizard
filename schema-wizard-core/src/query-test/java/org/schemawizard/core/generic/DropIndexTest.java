package org.schemawizard.core.generic;

import org.junit.jupiter.api.Test;
import org.schemawizard.core.config.GenericTest;
import org.schemawizard.core.config.extension.DisableFor;
import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.migration.builder.operation.DropIndex;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.Operation;

public class DropIndexTest extends GenericTest {
    @Test
    @DisableFor(DatabaseProvider.MYSQL)
    public void shouldGenerateDropIndexWithName() {
        Operation operation = DropIndex.builder()
                .name("idx_users_email")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("drop-index/name", info.getSql());
    }

    @Test
    @DisableFor(DatabaseProvider.MYSQL)
    public void shouldGenerateDropIndexWithSchemaAndName() {
        Operation operation = DropIndex.builder()
                .schema("schemawizard")
                .name("idx_users_email")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("drop-index/schema-and-name", info.getSql());
    }

    @Test
    @DisableFor(DatabaseProvider.MYSQL)
    public void shouldGenerateDropIndexIfExists() {
        Operation operation = DropIndex.builder()
                .name("idx_users_email")
                .ifExists()
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("drop-index/if-exists", info.getSql());
    }

    @Test
    @DisableFor({DatabaseProvider.POSTGRESQL, DatabaseProvider.ORACLE})
    public void shouldGenerateDropIndexWithNameAndTable() {
        Operation operation = DropIndex.builder()
                .name("idx_users_email")
                .on("users")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("drop-index/with-table/name", info.getSql());
    }

    @Test
    @DisableFor({DatabaseProvider.POSTGRESQL, DatabaseProvider.ORACLE})
    public void shouldGenerateDropIndexIfExistsWithTable() {
        Operation operation = DropIndex.builder()
                .name("idx_users_email")
                .ifExists()
                .on("users")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("drop-index/with-table/if-exists", info.getSql());
    }
}
