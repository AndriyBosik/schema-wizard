package org.schemawizard.core.migration.operation.resolver.postgresql;

import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.metadata.SqlClause;
import org.schemawizard.core.migration.annotation.Provider;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.DropIndexOperation;
import org.schemawizard.core.migration.operation.resolver.OperationResolver;

@Provider(DatabaseProvider.POSTGRESQL)
public class PostgreSqlDropIndexOperationResolver implements OperationResolver<DropIndexOperation> {
    @Override
    public MigrationInfo resolve(DropIndexOperation operation) {
        return new MigrationInfo(
                String.format(
                        "%s %s%s",
                        SqlClause.DROP_INDEX,
                        operation.isIfExists() ? (SqlClause.IF_EXISTS + " ") : "",
                        operation.getSchema() == null ? operation.getName() : (operation.getSchema() + "." + operation.getName())));
    }
}
