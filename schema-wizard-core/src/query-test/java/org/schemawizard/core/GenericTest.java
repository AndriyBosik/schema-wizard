package org.schemawizard.core;

import org.schemawizard.core.di.DiContainer;
import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.migration.annotation.Provider;
import org.schemawizard.core.migration.factory.ColumnTypeFactory;
import org.schemawizard.core.migration.factory.impl.OracleColumnTypeFactory;
import org.schemawizard.core.migration.factory.impl.PostgreSqlColumnTypeFactory;
import org.schemawizard.core.migration.operation.Operation;
import org.schemawizard.core.migration.operation.resolver.OperationResolver;
import org.schemawizard.core.migration.operation.resolver.multi.MultiNativeQueryFileOperationResolver;
import org.schemawizard.core.migration.operation.resolver.multi.MultiNativeQueryRawOperationResolver;
import org.schemawizard.core.migration.operation.resolver.oracle.OracleAddColumnOperationResolver;
import org.schemawizard.core.migration.operation.resolver.oracle.OracleAddColumnsOperationResolver;
import org.schemawizard.core.migration.operation.resolver.oracle.OracleAddForeignKeyOperationResolver;
import org.schemawizard.core.migration.operation.resolver.oracle.OracleAddPrimaryKeyOperationResolver;
import org.schemawizard.core.migration.operation.resolver.oracle.OracleAddUniqueOperationResolver;
import org.schemawizard.core.migration.operation.resolver.oracle.OracleCreateTableOperationResolver;
import org.schemawizard.core.migration.operation.resolver.oracle.OracleDropColumnOperationResolver;
import org.schemawizard.core.migration.operation.resolver.oracle.OracleDropColumnsOperationResolver;
import org.schemawizard.core.migration.operation.resolver.oracle.OracleDropForeignKeyOperationResolver;
import org.schemawizard.core.migration.operation.resolver.oracle.OracleDropPrimaryKeyOperationResolver;
import org.schemawizard.core.migration.operation.resolver.oracle.OracleDropTableOperationResolver;
import org.schemawizard.core.migration.operation.resolver.oracle.OracleDropUniqueOperationResolver;
import org.schemawizard.core.migration.operation.resolver.postgresql.PostgreSqlAddColumnOperationResolver;
import org.schemawizard.core.migration.operation.resolver.postgresql.PostgreSqlAddColumnsOperationResolver;
import org.schemawizard.core.migration.operation.resolver.postgresql.PostgreSqlAddForeignKeyOperationResolver;
import org.schemawizard.core.migration.operation.resolver.postgresql.PostgreSqlAddPrimaryKeyOperationResolver;
import org.schemawizard.core.migration.operation.resolver.postgresql.PostgreSqlAddUniqueOperationResolver;
import org.schemawizard.core.migration.operation.resolver.postgresql.PostgreSqlCreateTableOperationResolver;
import org.schemawizard.core.migration.operation.resolver.postgresql.PostgreSqlDropColumnOperationResolver;
import org.schemawizard.core.migration.operation.resolver.postgresql.PostgreSqlDropColumnsOperationResolver;
import org.schemawizard.core.migration.operation.resolver.postgresql.PostgreSqlDropForeignKeyOperationResolver;
import org.schemawizard.core.migration.operation.resolver.postgresql.PostgreSqlDropPrimaryKeyOperationResolver;
import org.schemawizard.core.migration.operation.resolver.postgresql.PostgreSqlDropTableOperationResolver;
import org.schemawizard.core.migration.operation.resolver.postgresql.PostgreSqlDropUniqueOperationResolver;
import org.schemawizard.core.migration.service.OperationResolverService;
import org.schemawizard.core.migration.service.OperationService;
import org.schemawizard.core.migration.service.impl.OperationResolverServiceImpl;
import org.schemawizard.core.migration.service.impl.OperationServiceImpl;
import org.schemawizard.core.model.ConfigurationProperties;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

public class GenericTest {
    protected final OperationResolverService operationResolverService;

    public GenericTest() {
        ConfigurationProperties properties = ConfigurationProperties.builder()
                .databaseProvider(DatabaseProvider.POSTGRESQL)
                .build();

        DiContainer container = new DiContainer();

        container.register(ConfigurationProperties.class, properties);
        container.register(OperationService.class, OperationServiceImpl.class);
        container.register(ColumnTypeFactory.class, PostgreSqlColumnTypeFactory.class);
        container.register(ColumnTypeFactory.class, OracleColumnTypeFactory.class);

        DatabaseProvider provider = properties.getDatabaseProvider();

        Set.of(
                        MultiNativeQueryFileOperationResolver.class,
                        MultiNativeQueryRawOperationResolver.class,
                        PostgreSqlAddColumnOperationResolver.class,
                        PostgreSqlAddColumnsOperationResolver.class,
                        PostgreSqlAddForeignKeyOperationResolver.class,
                        PostgreSqlAddPrimaryKeyOperationResolver.class,
                        PostgreSqlAddUniqueOperationResolver.class,
                        PostgreSqlCreateTableOperationResolver.class,
                        PostgreSqlDropColumnOperationResolver.class,
                        PostgreSqlDropColumnsOperationResolver.class,
                        PostgreSqlDropForeignKeyOperationResolver.class,
                        PostgreSqlDropPrimaryKeyOperationResolver.class,
                        PostgreSqlDropTableOperationResolver.class,
                        PostgreSqlDropUniqueOperationResolver.class,
                        OracleAddColumnOperationResolver.class,
                        OracleAddColumnsOperationResolver.class,
                        OracleAddForeignKeyOperationResolver.class,
                        OracleAddPrimaryKeyOperationResolver.class,
                        OracleAddUniqueOperationResolver.class,
                        OracleCreateTableOperationResolver.class,
                        OracleDropColumnOperationResolver.class,
                        OracleDropColumnsOperationResolver.class,
                        OracleDropForeignKeyOperationResolver.class,
                        OracleDropPrimaryKeyOperationResolver.class,
                        OracleDropTableOperationResolver.class,
                        OracleDropUniqueOperationResolver.class).stream()
                .map(resolver -> new AbstractMap.SimpleEntry<>(resolver, parserDatabaseProviderFromClass(resolver)))
                .filter(pair -> pair.getValue() == provider || pair.getValue() == DatabaseProvider.MULTI)
                .map(Map.Entry::getKey)
                .forEach(resolver -> container.register(OperationResolver.class, resolver));

        container.register(OperationResolverService.class, OperationResolverServiceImpl.class);

        this.operationResolverService = container.resolve(OperationResolverService.class);
    }

    private static DatabaseProvider parserDatabaseProviderFromClass(Class<? extends OperationResolver<? extends Operation>> resolver) {
        Provider annotation = resolver.getAnnotation(Provider.class);
        if (annotation == null) {
            return DatabaseProvider.MULTI;
        }
        return annotation.value();
    }
}
