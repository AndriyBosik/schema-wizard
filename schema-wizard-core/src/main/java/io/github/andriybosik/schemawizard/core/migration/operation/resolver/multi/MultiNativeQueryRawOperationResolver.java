package io.github.andriybosik.schemawizard.core.migration.operation.resolver.multi;

import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;
import io.github.andriybosik.schemawizard.core.migration.annotation.Provider;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationInfo;
import io.github.andriybosik.schemawizard.core.migration.operation.NativeQueryRawOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.resolver.OperationResolver;

@Provider(DatabaseProvider.MULTI)
public class MultiNativeQueryRawOperationResolver implements OperationResolver<NativeQueryRawOperation> {
    @Override
    public MigrationInfo resolve(NativeQueryRawOperation operation) {
        return new MigrationInfo(
                operation.getSqlQuery());
    }
}
