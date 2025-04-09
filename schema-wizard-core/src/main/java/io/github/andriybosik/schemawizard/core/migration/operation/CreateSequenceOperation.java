package io.github.andriybosik.schemawizard.core.migration.operation;

public class CreateSequenceOperation implements Operation {
    private final String schema;
    private final String name;
    private final int increment;
    private final Integer minValue;
    private final Integer maxValue;
    private final Integer start;
    private final Integer cache;
    private final boolean cycle;
    private final boolean ifNotExists;

    public CreateSequenceOperation(
            String schema,
            String name,
            int increment,
            Integer minValue,
            Integer maxValue,
            Integer start,
            Integer cache,
            boolean cycle,
            boolean ifNotExists
    ) {
        this.schema = schema;
        this.name = name;
        this.increment = increment;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.start = start;
        this.cache = cache;
        this.cycle = cycle;
        this.ifNotExists = ifNotExists;
    }

    public String getSchema() {
        return schema;
    }

    public String getName() {
        return name;
    }

    public int getIncrement() {
        return increment;
    }

    public Integer getMinValue() {
        return minValue;
    }

    public Integer getMaxValue() {
        return maxValue;
    }

    public Integer getStart() {
        return start;
    }

    public Integer getCache() {
        return cache;
    }

    public boolean isCycle() {
        return cycle;
    }

    public boolean isIfNotExists() {
        return ifNotExists;
    }
}
