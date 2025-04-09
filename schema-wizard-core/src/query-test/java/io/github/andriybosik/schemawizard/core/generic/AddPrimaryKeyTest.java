package io.github.andriybosik.schemawizard.core.generic;

import org.junit.jupiter.api.Test;
import io.github.andriybosik.schemawizard.core.config.GenericTest;
import io.github.andriybosik.schemawizard.core.migration.builder.operation.AddPrimaryKey;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationInfo;
import io.github.andriybosik.schemawizard.core.migration.operation.Operation;

public class AddPrimaryKeyTest extends GenericTest {
    @Test
    public void shouldGenerateAddPrimaryKeyForTable() {
        Operation operation = AddPrimaryKey.builder("users")
                .name("pk_users_id")
                .columns("id")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("add-primary-key/table", info.getSql());
    }

    @Test
    public void shouldGenerateAddPrimaryKeyForSchemaAndTable() {
        Operation operation = AddPrimaryKey.builder("schemawizard", "users")
                .name("pk_users_id")
                .columns("id")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("add-primary-key/schema-and-table", info.getSql());
    }

    @Test
    public void shouldGenerateAddPrimaryKeyForMultipleColumns() {
        Operation operation = AddPrimaryKey.builder("users")
                .name("pk_users_first_name_last_name")
                .columns("first_name", "last_name")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("add-primary-key/multiple-columns", info.getSql());
    }

    @Test
    public void shouldMapCamelCaseColumnsToSnakeCase() {
        Operation operation = AddPrimaryKey.builder("users")
                .name("pk_users_first_name_last_name")
                .columns("firstName", "lastName")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("add-primary-key/map-camel-case-to-snake-case", info.getSql());
    }
}
