package com.example.migration.operation.resolver.multi;

import com.example.metadata.DatabaseProvider;
import com.example.migration.annotation.Provider;
import com.example.migration.model.MigrationInfo;
import com.example.migration.operation.NativeQueryRawOperation;
import com.example.migration.operation.resolver.OperationResolver;

@Provider(DatabaseProvider.MULTI)
public class MultiNativeQueryRawOperationResolver implements OperationResolver<NativeQueryRawOperation> {
    @Override
    public MigrationInfo resolve(NativeQueryRawOperation operation) {
        return new MigrationInfo(
                operation.getSqlQuery());
    }
}
