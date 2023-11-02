package com.example.migration.operation.resolver.custom;

import com.example.metadata.DatabaseProvider;
import com.example.migration.annotation.Provider;
import com.example.migration.model.MigrationInfo;
import com.example.migration.operation.DropTableOperation;
import com.example.migration.operation.resolver.OperationResolver;

@Provider(DatabaseProvider.POSTGRESQL)
public class CustomDropTableOperationResolver implements OperationResolver<DropTableOperation> {
    @Override
    public MigrationInfo resolve(DropTableOperation operation) {
        return null;
    }
}
