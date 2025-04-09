package com.example.extension.resolver;

import com.example.extension.resolver.operation.DropEnumOperation;
import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;
import io.github.andriybosik.schemawizard.core.migration.annotation.Provider;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationInfo;
import io.github.andriybosik.schemawizard.core.migration.operation.resolver.OperationResolver;

@Provider(DatabaseProvider.POSTGRESQL)
public class PostgreSqlDropEnumOperationResolver implements OperationResolver<DropEnumOperation> {
    @Override
    public MigrationInfo resolve(DropEnumOperation operation) {
        return new MigrationInfo(
                String.format(
                        "DROP TYPE %s.%s",
                        operation.getSchema(),
                        operation.getEnumName()
                )
        );
    }
}
