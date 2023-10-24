package com.example.migration.operation;

public class AddColumnOperation implements Operation {
    private final String schema;
    private final String table;
    private final String name;
    private final String type;
    private final Integer minLength;
    private final Integer maxLength;
    private final Integer precision;
    private final Integer scale;
    private final boolean nullable;
    private final String sqlDefault;

    public AddColumnOperation(
            String schema,
            String table,
            String name,
            String type,
            Integer minLength,
            Integer maxLength,
            Integer precision,
            Integer scale,
            boolean nullable,
            String sqlDefault
    ) {
        this.schema = schema;
        this.table = table;
        this.name = name;
        this.type = type;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.precision = precision;
        this.scale = scale;
        this.nullable = nullable;
        this.sqlDefault = sqlDefault;
    }

    public String getSchema() {
        return schema;
    }

    public String getTable() {
        return table;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public Integer getPrecision() {
        return precision;
    }

    public Integer getScale() {
        return scale;
    }

    public boolean isNullable() {
        return nullable;
    }

    public String getSqlDefault() {
        return sqlDefault;
    }
}
