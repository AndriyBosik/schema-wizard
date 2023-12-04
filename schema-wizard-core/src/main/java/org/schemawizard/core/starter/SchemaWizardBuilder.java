package org.schemawizard.core.starter;

import org.schemawizard.core.analyzer.HistoryTableCreator;
import org.schemawizard.core.analyzer.MigrationAnalyzer;
import org.schemawizard.core.analyzer.impl.HistoryTableCreatorImpl;
import org.schemawizard.core.analyzer.impl.MigrationAnalyzerImpl;
import org.schemawizard.core.analyzer.service.AppliedMigrationsService;
import org.schemawizard.core.analyzer.service.DeclaredMigrationService;
import org.schemawizard.core.analyzer.service.impl.AppliedMigrationsServiceImpl;
import org.schemawizard.core.analyzer.service.impl.ClassesDeclaredMigrationService;
import org.schemawizard.core.callback.Callback;
import org.schemawizard.core.callback.QueryGeneratedCallback;
import org.schemawizard.core.callback.impl.QueryLoggerCallback;
import org.schemawizard.core.dao.ConnectionHolder;
import org.schemawizard.core.dao.HistoryTableQueryFactory;
import org.schemawizard.core.dao.impl.PostgresHistoryTableQueryFactory;
import org.schemawizard.core.di.DiContainer;
import org.schemawizard.core.exception.InvalidConfigurationException;
import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.metadata.ErrorMessage;
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
import org.schemawizard.core.property.service.ConfigurationPropertiesService;
import org.schemawizard.core.property.service.PropertyParser;
import org.schemawizard.core.property.service.impl.CamelCasePropertyUtils;
import org.schemawizard.core.property.service.impl.ConfigurationPropertiesServiceImpl;
import org.schemawizard.core.property.service.impl.PropertyParserImpl;
import org.schemawizard.core.runner.MigrationRunner;
import org.schemawizard.core.runner.impl.MigrationRunnerImpl;
import org.schemawizard.core.service.TransactionService;
import org.schemawizard.core.service.impl.TransactionServiceImpl;
import org.yaml.snakeyaml.introspector.PropertyUtils;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class SchemaWizardBuilder {
    private final DiContainer container;
    private final ConfigurationProperties properties;

    private SchemaWizardBuilder(
            DiContainer container,
            ConfigurationProperties properties
    ) {
        this.container = container;
        this.properties = properties;
    }

    public static SchemaWizardBuilder init() {
        DiContainer container = new DiContainer();
        container.register(PropertyUtils.class, CamelCasePropertyUtils.class);
        container.register(PropertyParser.class, PropertyParserImpl.class);
        container.register(ConfigurationPropertiesService.class, ConfigurationPropertiesServiceImpl.class);

        ConfigurationPropertiesService propertiesService = container.resolve(ConfigurationPropertiesService.class);
        ConfigurationProperties properties = propertiesService.getProperties();
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

        container.register(TransactionService.class, TransactionServiceImpl.class);
        container.register(HistoryTableQueryFactory.class, getHistoryTableQueryFactoryClass(provider));
        container.register(AppliedMigrationsService.class, AppliedMigrationsServiceImpl.class);
        container.register(DeclaredMigrationService.class, ClassesDeclaredMigrationService.class);
        container.register(ConnectionHolder.class, ConnectionHolder.class);
        container.register(HistoryTableCreator.class, HistoryTableCreatorImpl.class);
        container.register(MigrationAnalyzer.class, MigrationAnalyzerImpl.class);
        container.register(MigrationRunner.class, MigrationRunnerImpl.class);
        container.register(OperationResolverService.class, OperationResolverServiceImpl.class);
        container.register(SchemaWizard.class, SchemaWizard.class);

        if (properties.isLogGeneratedSql()) {
            container.register(QueryGeneratedCallback.class, QueryLoggerCallback.class);
        }

        return new SchemaWizardBuilder(container, properties);
    }

    public SchemaWizardBuilder registerResolver(Class<? extends OperationResolver<?>> resolverClass) {
        DatabaseProvider resolverProvider = parserDatabaseProviderFromClass(resolverClass);
        DatabaseProvider propertiesProvider = properties.getDatabaseProvider();
        if (Objects.equals(resolverProvider, propertiesProvider) || Objects.equals(resolverProvider, DatabaseProvider.MULTI)) {
            container.register(OperationResolver.class, resolverClass);
        }
        return this;
    }

    public <T extends Callback, R extends T> SchemaWizardBuilder registerCallback(Class<T> baseType, Class<R> instanceType) {
        container.register(baseType, instanceType);
        return this;
    }

    public SchemaWizard build() {
        return container.resolve(SchemaWizard.class);
    }

    private static DatabaseProvider parserDatabaseProviderFromClass(Class<? extends OperationResolver<? extends Operation>> resolver) {
        Provider annotation = resolver.getAnnotation(Provider.class);
        if (annotation == null) {
            return DatabaseProvider.MULTI;
        }
        return annotation.value();
    }

    private static Class<? extends HistoryTableQueryFactory> getHistoryTableQueryFactoryClass(DatabaseProvider provider) {
        if (provider == DatabaseProvider.POSTGRESQL) {
            return PostgresHistoryTableQueryFactory.class;
        }
        throw new InvalidConfigurationException(String.format(
                ErrorMessage.NO_HISTORY_TABLE_QUERY_FACTORY_FOUND_FORMAT,
                provider));
    }
}
