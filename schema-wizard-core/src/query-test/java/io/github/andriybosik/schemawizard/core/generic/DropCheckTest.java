package io.github.andriybosik.schemawizard.core.generic;

import org.junit.jupiter.api.Test;
import io.github.andriybosik.schemawizard.core.config.GenericTest;
import io.github.andriybosik.schemawizard.core.migration.builder.operation.DropCheck;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationInfo;
import io.github.andriybosik.schemawizard.core.migration.operation.Operation;

public class DropCheckTest extends GenericTest {
    @Test
    public void shouldGenerateDropCheckForTable() {
        Operation operation = DropCheck.builder("users")
                .name("chk_users_age_greater_than_zero")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("drop-check/table", info.getSql());
    }

    @Test
    public void shouldGenerateDropCheckForSchemaAndTable() {
        Operation operation = DropCheck.builder("schemawizard", "users")
                .name("chk_users_age_greater_than_zero")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("drop-check/schema-and-table", info.getSql());
    }

    @Test
    public void shouldGenerateDropCheckIfExists() {
        Operation operation = DropCheck.builder("users")
                .name("chk_users_age_greater_than_zero")
                .ifExists()
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("drop-check/if-exists", info.getSql());
    }
}
