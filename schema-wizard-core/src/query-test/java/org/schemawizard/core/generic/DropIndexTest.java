package org.schemawizard.core.generic;

import org.junit.jupiter.api.Test;
import org.schemawizard.core.config.GenericTest;
import org.schemawizard.core.migration.builder.operation.DropIndex;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.Operation;

public class DropIndexTest extends GenericTest {
    @Test
    public void shouldGenerateDropIndexWithName() {
        Operation operation = DropIndex.builder()
                .name("idx_users_email")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("drop-index/name", info.getSql());
    }

    @Test
    public void shouldGenerateDropIndexWithSchemaAndName() {
        Operation operation = DropIndex.builder()
                .schema("schemawizard")
                .name("idx_users_email")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("drop-index/schema-and-name", info.getSql());
    }

    @Test
    public void shouldGenerateDropIndexIfExists() {
        Operation operation = DropIndex.builder()
                .name("idx_users_email")
                .ifExists()
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("drop-index/if-exists", info.getSql());
    }
}
