package io.github.andriybosik.schemawizard.core.migration.operation;

public abstract class TableBasedOperation implements Operation {
    protected final String schema;
    protected final String table;

    public TableBasedOperation(String schema, String table) {
        this.schema = schema;
        this.table = table;
    }

    public String getSchema() {
        return schema;
    }

    public String getTable() {
        return table;
    }
}
