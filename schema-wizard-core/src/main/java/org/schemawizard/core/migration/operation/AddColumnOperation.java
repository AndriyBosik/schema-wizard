package org.schemawizard.core.migration.operation;

public class AddColumnOperation extends TableBasedOperation {
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
        super(schema, table);
        this.name = name;
        this.type = type;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.precision = precision;
        this.scale = scale;
        this.nullable = nullable;
        this.sqlDefault = sqlDefault;
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
