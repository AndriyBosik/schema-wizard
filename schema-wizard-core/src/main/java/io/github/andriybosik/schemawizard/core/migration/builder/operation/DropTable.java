package io.github.andriybosik.schemawizard.core.migration.builder.operation;

import io.github.andriybosik.schemawizard.core.metadata.ErrorMessage;
import io.github.andriybosik.schemawizard.core.migration.operation.DropTableOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.Operation;
import io.github.andriybosik.schemawizard.core.exception.InvalidMigrationDefinitionException;
import io.github.andriybosik.schemawizard.core.utils.StringUtils;

public class DropTable<T> implements OperationBuilder {
    private final String schema;
    private final String table;
    private boolean ifExists;

    private DropTable(
            String schema,
            String table
    ) {
        this.schema = schema;
        this.table = table;
        this.ifExists = false;
    }

    public static <T> DropTable<T> builder(String schema, String table) {
        return new DropTable<>(schema, table);
    }

    public static <T> DropTable<T> builder(String table) {
        return new DropTable<>(null, table);
    }

    public DropTable<T> ifExists() {
        this.ifExists = true;
        return this;
    }

    @Override
    public Operation build() {
        if (StringUtils.isBlank(table)) {
            throw new InvalidMigrationDefinitionException(ErrorMessage.BLANK_TABLE_NAME);
        }
        return new DropTableOperation(schema, table, ifExists);
    }
}
