package org.schemawizard.core.migration.operation.resolver;

import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.Operation;

public interface OperationResolver<T extends Operation> {
    MigrationInfo resolve(T operation);
}
