package org.schemawizard.core.migration.operation.resolver.postgresql;

import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.metadata.SqlClause;
import org.schemawizard.core.migration.annotation.Provider;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.CreateSequenceOperation;
import org.schemawizard.core.migration.operation.resolver.OperationResolver;
import org.schemawizard.core.utils.StringUtils;

@Provider(DatabaseProvider.POSTGRESQL)
public class PostgreSqlCreateSequenceOperationResolver implements OperationResolver<CreateSequenceOperation> {
    @Override
    public MigrationInfo resolve(CreateSequenceOperation operation) {
        return new MigrationInfo(
                String.format(
                        "%s %s%s%s%s%s%s%s%s",
                        SqlClause.CREATE_SEQUENCE,
                        operation.isIfNotExists() ? (SqlClause.IF_NOT_EXISTS + " ") : "",
                        buildName(operation),
                        String.format(" %s %s", SqlClause.INCREMENT, operation.getIncrement()),
                        operation.getMinValue() == null ? "" : String.format(" %s %s", SqlClause.MINVALUE, operation.getMinValue()),
                        operation.getMaxValue() == null ? "" : String.format(" %s %s", SqlClause.MAXVALUE, operation.getMaxValue()),
                        operation.getStart() == null ? "" : String.format(" %s %s", SqlClause.START, operation.getStart()),
                        operation.getCache() == null ? "" : String.format(" %s %s", SqlClause.CACHE, operation.getCache()),
                        operation.isCycle() ? SqlClause.CYCLE : SqlClause.NO_CYCLE
                )
        );
    }

    private String buildName(CreateSequenceOperation operation) {
        if (StringUtils.isNotBlank(operation.getSchema())) {
            return String.format("%s.%s", operation.getSchema(), operation.getName());
        }
        return operation.getName();
    }
}
