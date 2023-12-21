package com.example.extension;

import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.resolver.OperationResolver;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CreateEnumOperationResolver implements OperationResolver<CreateEnumOperation> {
    @Override
    public MigrationInfo resolve(CreateEnumOperation operation) {
        return new MigrationInfo(
                String.format(
                        "CREATE TYPE %s.%s AS ENUM (%s)",
                        operation.getSchema(),
                        operation.getEnumName(),
                        Arrays.stream(operation.getValues())
                                .collect(Collectors.joining("', '", "'", "'"))
                )
        );
    }
}
