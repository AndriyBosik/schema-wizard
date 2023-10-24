package com.example.migration.builder.operation;

import com.example.migration.operation.DropTableOperation;
import com.example.migration.operation.Operation;
import com.example.exception.InvalidMigrationMetadataException;
import com.example.utils.StringUtils;

public class DropTable<T> implements OperationBuilder {
    private final String schema;
    private final String table;
    private boolean checkIfExists;

    private DropTable(
            String schema,
            String table
    ) {
        this.schema = schema;
        this.table = table;
    }

    public static <T> DropTable<T> builder(String schema, String table) {
        return new DropTable<>(schema, table);
    }

    public static <T> DropTable<T> builder(String table) {
        return new DropTable<>(null, table);
    }

    public DropTable<T> checkIfExists(boolean ifExists) {
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
