package io.github.andriybosik.schemawizard.core.migration.operation;

public class DropIndexOperation implements Operation {
    private final String schema;
    private final String name;
    private final String tableSchema;
    private final String tableName;
    private final boolean ifExists;

    public DropIndexOperation(String schema, String name, boolean ifExists, String tableSchema, String tableName) {
        this.schema = schema;
        this.name = name;
        this.ifExists = ifExists;
        this.tableSchema = tableSchema;
        this.tableName = tableName;
    }

    public String getSchema() {
        return schema;
    }

    public String getName() {
        return name;
    }

    public boolean isIfExists() {
        return ifExists;
    }

    public String getTableSchema() {
        return tableSchema;
    }

    public String getTableName() {
        return tableName;
    }
}
