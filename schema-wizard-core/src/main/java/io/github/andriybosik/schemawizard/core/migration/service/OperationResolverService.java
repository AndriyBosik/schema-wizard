package io.github.andriybosik.schemawizard.core.migration.service;

import io.github.andriybosik.schemawizard.core.migration.model.MigrationInfo;
import io.github.andriybosik.schemawizard.core.migration.operation.Operation;

public interface OperationResolverService {
    MigrationInfo resolve(Operation operation);
}
