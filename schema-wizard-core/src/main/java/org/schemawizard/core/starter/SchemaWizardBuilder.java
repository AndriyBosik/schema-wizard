package org.schemawizard.core.starter;

import org.reflections.Reflections;
import org.schemawizard.core.analyzer.HistoryTableCreator;
import org.schemawizard.core.analyzer.MigrationAnalyzer;
import org.schemawizard.core.analyzer.factory.DowngradeFactory;
import org.schemawizard.core.analyzer.factory.impl.DowngradeFactoryImpl;
import org.schemawizard.core.analyzer.impl.HistoryTableCreatorImpl;
import org.schemawizard.core.analyzer.impl.MigrationAnalyzerImpl;
import org.schemawizard.core.analyzer.service.AppliedMigrationsService;
import org.schemawizard.core.analyzer.service.DeclaredMigrationService;
import org.schemawizard.core.analyzer.service.impl.AppliedMigrationsServiceImpl;
import org.schemawizard.core.analyzer.service.impl.ClassesDeclaredMigrationService;
import org.schemawizard.core.callback.AfterQueryExecutionCallback;
import org.schemawizard.core.callback.BeforeQueryExecutionCallback;
import org.schemawizard.core.callback.QueryGeneratedCallback;
import org.schemawizard.core.callback.impl.QueryLoggerCallback;
import org.schemawizard.core.dao.ConnectionHolder;
import org.schemawizard.core.dao.HistoryTableQueryFactory;
import org.schemawizard.core.dao.TransactionService;
import org.schemawizard.core.dao.impl.OracleHistoryTableQueryFactory;
import org.schemawizard.core.dao.impl.PostgresHistoryTableQueryFactory;
import org.schemawizard.core.dao.impl.TransactionServiceImpl;
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
import org.yaml.snakeyaml.introspector.PropertyUtils;

import java.util.AbstractMap;
import java.util.Map;

public class SchemaWizardBuilder {
    private final DiContainer container;
    private final ConfigurationProperties properties;

    private static final String SW_BASE_PACKAGE_NAME = "org.schemawizard.core";

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

        container.register(DowngradeFactory.class, DowngradeFactoryImpl.class);
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
        Reflections basePackageReflections = new Reflections(SW_BASE_PACKAGE_NAME);
        registerResolvers(basePackageReflections, container, provider);

        if (properties.getExtensionPackage() != null) {
            Reflections extencionPackageReflections = new Reflections(properties.getExtensionPackage());
            registerResolvers(extencionPackageReflections, container, provider);

            var beforeQueryCallbacks = extencionPackageReflections.getSubTypesOf(BeforeQueryExecutionCallback.class);
            beforeQueryCallbacks.forEach(callback -> container.register(BeforeQueryExecutionCallback.class, callback));

            var afterQueryCallbacks = extencionPackageReflections.getSubTypesOf(AfterQueryExecutionCallback.class);
            afterQueryCallbacks.forEach(callback -> container.register(AfterQueryExecutionCallback.class, callback));
        }

        return new SchemaWizardBuilder(container, properties);
    }

    @SuppressWarnings("unchecked")
    private static void registerResolvers(Reflections reflections, DiContainer container, DatabaseProvider provider) {
        reflections.getSubTypesOf(OperationResolver.class).stream()
                .map(resolver -> new AbstractMap.SimpleEntry<>(
                        resolver, parserDatabaseProviderFromClass((Class<? extends OperationResolver<?>>) resolver)))
                .filter(pair -> pair.getValue() == provider || pair.getValue() == DatabaseProvider.MULTI)
                .map(Map.Entry::getKey)
                .forEach(resolver -> container.register(OperationResolver.class, resolver));
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
        if (provider == DatabaseProvider.ORACLE) {
            return OracleHistoryTableQueryFactory.class;
        }
        throw new InvalidConfigurationException(String.format(
                ErrorMessage.NO_HISTORY_TABLE_QUERY_FACTORY_FOUND_FORMAT,
                provider));
    }
}
