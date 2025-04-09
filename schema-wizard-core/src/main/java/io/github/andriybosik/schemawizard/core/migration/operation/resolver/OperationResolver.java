package io.github.andriybosik.schemawizard.core.migration.operation.resolver;

import io.github.andriybosik.schemawizard.core.migration.model.MigrationInfo;
import io.github.andriybosik.schemawizard.core.migration.operation.Operation;

public interface OperationResolver<T extends Operation> {
    MigrationInfo resolve(T operation);
}
