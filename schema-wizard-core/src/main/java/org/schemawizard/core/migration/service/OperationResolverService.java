package org.schemawizard.core.migration.service;

import org.schemawizard.core.migration.Migration;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.Operation;

public interface OperationResolverService {
    MigrationInfo resolve(Operation operation);
}
