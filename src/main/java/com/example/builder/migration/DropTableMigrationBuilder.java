package com.example.builder.migration;

import com.example.builder.operation.DropTableOperation;
import com.example.builder.operation.Operation;
import com.example.exception.InvalidMigrationMetadataException;
import com.example.utils.StringUtils;

public class DropTableMigrationBuilder<T> implements MigrationBuilder {
    private final String schema;
    private final String table;
    private boolean checkIfExists;

    private DropTableMigrationBuilder(
            String schema,
            String table
    ) {
        this.schema = schema;
        this.table = table;
    }

    public static <T> DropTableMigrationBuilder<T> builder(String schema, String table) {
        return new DropTableMigrationBuilder<>(schema, table);
    }

    public static <T> DropTableMigrationBuilder<T> builder(String table) {
        return new DropTableMigrationBuilder<>(null, table);
    }

    public DropTableMigrationBuilder<T> checkIfExists(boolean ifExists) {
        this.checkIfExists = ifExists;
        return this;
    }

    @Override
    public Operation build() {
        if (StringUtils.isBlank(table)) {
            throw new InvalidMigrationMetadataException("Table name must not be blank");
        }
        return new DropTableOperation(schema, table, checkIfExists);
    }
}
