package com.example.migration.operation.resolver.oracle;

import com.example.metadata.DatabaseProvider;
import com.example.migration.annotation.Provider;
import com.example.migration.model.MigrationInfo;
import com.example.migration.operation.AddForeignKeyOperation;
import com.example.migration.operation.AddPrimaryKeyOperation;
import com.example.migration.operation.AddUniqueOperation;
import com.example.migration.operation.CreateTableOperation;
import com.example.migration.operation.resolver.OperationResolver;
import com.example.migration.service.OperationService;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Provider(DatabaseProvider.ORACLE)
public class OracleCreateTableOperationResolver implements OperationResolver<CreateTableOperation> {
    private final OperationService operationService;

    public OracleCreateTableOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(CreateTableOperation operation) {
        return new MigrationInfo(
                String.format(
                        "CREATE TABLE%s %s (%s, %s)",
                        operation.isIfNotExists() ? " IF NOT EXISTS" : "",
                        operationService.buildTable(operation),
                        buildColumnsDefinitions(operation),
                        buildConstraints(operation)));
    }

    private String buildColumnsDefinitions(CreateTableOperation operation) {
        return operation.getColumns().stream()
                .map(operationService::buildColumnDefinition)
                .collect(Collectors.joining(", "));
    }

    private String buildConstraints(CreateTableOperation operation) {
        return Stream.of(
                        Stream.of(buildPrimaryKey(operation.getPrimaryKey())),
                        operation.getForeignKeys().stream().map(this::buildForeignKey),
                        operation.getUniques().stream().map(this::buildUnique))
                .flatMap(Function.identity())
                .filter(Objects::nonNull)
                .collect(Collectors.joining(","));
    }

    private String buildPrimaryKey(AddPrimaryKeyOperation operation) {
        if (operation == null) {
            return null;
        }
        return String.format(
                "%sPRIMARY KEY (%s)",
                operation.getName() == null ? "" : "CONSTRAINT " + operation.getName() + " ",
                String.join(",", operation.getColumns()));
    }

    private String buildForeignKey(AddForeignKeyOperation operation) {
        if (operation == null) {
            return null;
        }
        return String.format(
                "%sFOREIGN KEY (%s) REFERENCES %s (%s)",
                operation.getName() == null ? "" : "CONSTRAINT " + operation.getName() + " ",
                String.join(",", operation.getColumns()),
                operationService.buildTable(operation.getForeignSchema(), operation.getForeignTable()),
                String.join(",", operation.getForeignColumns()));
    }

    private String buildUnique(AddUniqueOperation operation) {
        if (operation == null) {
            return null;
        }
        return String.format(
                "%sUNIQUE (%s)",
                operation.getName() == null ? "" : "CONSTRAINT " + operation.getName() + " ",
                String.join(",", operation.getColumns()));
    }
}
