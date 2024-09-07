package org.schemawizard.core.generic;

import org.junit.jupiter.api.Test;
import org.schemawizard.core.config.GenericTest;
import org.schemawizard.core.exception.InvalidMigrationDefinitionException;
import org.schemawizard.core.migration.builder.operation.AddColumns;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.Operation;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AddColumnsTest extends GenericTest {
    @Test
    public void shouldGenerateBooleanColumnNotNull() {
        Operation operation = AddColumns.builder("users", factory -> List.of(factory.newBool("enabled")
                        .nullable(false)))
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("add-columns/add-enabled-column", info.getSql());
    }

    @Test
    public void shouldGenerateDateColumn() {
        Operation operation = AddColumns.builder("users", factory -> List.of(factory.newDate("birthday").nullable(false)))
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("add-columns/add-date-column", info.getSql());
    }

    @Test
    public void shouldGenerateTimestampColumn() {
        Operation operation = AddColumns.builder("users", factory -> List.of(factory.newTimestamp("created_date")
                        .nullable(false)))
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("add-columns/add-timestamp-column", info.getSql());
    }

    @Test
    public void shouldGenerateTimestampWithTimeZoneColumn() {
        Operation operation = AddColumns.builder("users", factory -> List.of(factory.newTimestamp("created_date")
                        .withTimeZone(true)
                        .nullable(false)))
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("add-columns/add-timestamp-with-time-zone-column", info.getSql());
    }

    @Test
    public void shouldGenerateBooleanAndTextColumnsNullable() {
        Operation operation = AddColumns.builder(
                        "users",
                        factory -> List.of(
                                factory.newBool("enabled"),
                                factory.newText("first_name")
                                        .maxLength(200)))
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("add-columns/add-enabled-and-first-name-columns", info.getSql());
    }

    @Test
    public void shouldGenerateTextColumnWithMaxLength() {
        Operation operation = AddColumns.builder("schemawizard", "users", factory -> List.of(factory.newText("first_name").maxLength(200)))
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("add-columns/add-first-name-column-with-max-length", info.getSql());
    }

    @Test
    public void shouldGenerateGenericColumnWithSqlDefaultValue() {
        Operation operation = AddColumns.builder(
                        "users",
                        factory -> List.of(
                                factory.generic("age")
                                        .type("INTEGER")
                                        .nullable(false)
                                        .sqlDefault("5")))
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("add-columns/add-generic-column-with-sql-default", info.getSql());
    }

    @Test
    public void shouldGenerateDecimalColumn() {
        Operation operation = AddColumns.builder("users", factory -> List.of(
                        factory.newDecimal("age")
                                .nullable(false)
                                .precision(10)
                                .scale(5)))
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("add-columns/add-age-column", info.getSql());
    }

    @Test
    public void shouldGenerateAddColumnsIfNotExists() {
        Operation operation = AddColumns.builder("users", factory -> List.of(
                        factory.newText("first_name")
                                .maxLength(50)
                                .nullable(false)
                                .ifNotExists()))
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("add-columns/if-not-exists", info.getSql());
    }

    @Test
    public void shouldMapCamelCaseToSnakeCase() {
        Operation operation = AddColumns.builder("users", factory -> List.of(
                        factory.newText("firstName")
                                .maxLength(50)
                                .nullable(false)))
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("add-columns/map-camel-case-to-snake-case", info.getSql());
    }

    @Test
    public void shouldThrowExceptionWhenColumnNameIsEmpty() {
        InvalidMigrationDefinitionException exception = assertThrows(InvalidMigrationDefinitionException.class, () -> AddColumns.builder("users", factory -> List.of(
                        factory.newText("first_name").nullable(true),
                        factory.newText().nullable(true),
                        factory.newText("last_name").nullable(true)))
                .build());
        assertEquals("Empty column name for operation: AddColumns", exception.getMessage());
    }
}
