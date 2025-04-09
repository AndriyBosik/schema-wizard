package io.github.andriybosik.schemawizard.core.migration.builder.operation;

import io.github.andriybosik.schemawizard.core.migration.operation.CreateSequenceOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.Operation;

public class CreateSequence implements OperationBuilder {
    private final String schema;
    private final String name;
    private int increment = 1;
    private Integer minValue;
    private Integer maxValue;
    private Integer start;
    private Integer cache;
    private boolean cycle = false;
    private boolean ifNotExists = false;

    private CreateSequence(String schema, String name) {
        this.schema = schema;
        this.name = name;
    }

    public static CreateSequence builder(String name) {
        return builder(null, name);
    }

    public static CreateSequence builder(String schema, String name) {
        return new CreateSequence(schema, name);
    }

    public CreateSequence increment(int increment) {
        this.increment = increment;
        return this;
    }

    public CreateSequence minValue(int minValue) {
        this.minValue = minValue;
        return this;
    }

    public CreateSequence maxValue(int maxValue) {
        this.maxValue = maxValue;
        return this;
    }

    public CreateSequence start(int start) {
        this.start = start;
        return this;
    }

    public CreateSequence cache(int cache) {
        this.cache = cache;
        return this;
    }

    public CreateSequence cycle() {
        this.cycle = true;
        return this;
    }

    public CreateSequence ifNotExists() {
        this.ifNotExists = true;
        return this;
    }

    @Override
    public Operation build() {
        return new CreateSequenceOperation(
                schema,
                name,
                increment,
                minValue,
                maxValue,
                start,
                cache,
                cycle,
                ifNotExists);
    }
}
