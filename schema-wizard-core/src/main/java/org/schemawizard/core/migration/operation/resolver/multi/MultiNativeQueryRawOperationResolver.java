package org.schemawizard.core.migration.operation.resolver.multi;

import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.migration.annotation.Provider;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.NativeQueryRawOperation;
import org.schemawizard.core.migration.operation.resolver.OperationResolver;

@Provider(DatabaseProvider.MULTI)
public class MultiNativeQueryRawOperationResolver implements OperationResolver<NativeQueryRawOperation> {
    @Override
    public MigrationInfo resolve(NativeQueryRawOperation operation) {
        return new MigrationInfo(
                operation.getSqlQuery());
    }
}
