package com.example.extension.resolver;

import com.example.extension.resolver.operation.CreateEnumOperation;
import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;
import io.github.andriybosik.schemawizard.core.migration.annotation.Provider;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationInfo;
import io.github.andriybosik.schemawizard.core.migration.operation.resolver.OperationResolver;

import java.util.Arrays;
import java.util.stream.Collectors;

@Provider(DatabaseProvider.POSTGRESQL)
public class PostgreSqlCreateEnumOperationResolver implements OperationResolver<CreateEnumOperation> {
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
