package org.schemawizard.core.starter;

import org.reflections.Reflections;
import org.schemawizard.core.analyzer.HistoryTable;
import org.schemawizard.core.analyzer.MigrationAnalyzer;
import org.schemawizard.core.analyzer.factory.DowngradeFactory;
import org.schemawizard.core.analyzer.factory.ReflectionsFactory;
import org.schemawizard.core.analyzer.factory.impl.DowngradeFactoryImpl;
import org.schemawizard.core.analyzer.factory.impl.ReflectionsFactoryImpl;
import org.schemawizard.core.analyzer.impl.HistoryTableImpl;
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
import org.schemawizard.core.dao.DriverLoader;
import org.schemawizard.core.dao.HistoryTableQueryFactory;
import org.schemawizard.core.dao.TransactionService;
import org.schemawizard.core.dao.impl.DriverLoaderImpl;
import org.schemawizard.core.dao.impl.OracleHistoryTableQueryFactory;
import org.schemawizard.core.dao.impl.PostgresHistoryTableQueryFactory;
import org.schemawizard.core.dao.impl.TransactionServiceImpl;
import org.schemawizard.core.di.DiContainer;
import org.schemawizard.core.exception.InvalidConfigurationException;
import org.schemawizard.core.metadata.ColumnNamingStrategy;
import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.metadata.ErrorMessage;
import org.schemawizard.core.migration.annotation.Provider;
import org.schemawizard.core.migration.factory.ColumnTypeFactory;
import org.schemawizard.core.migration.factory.impl.OracleColumnTypeFactory;
import org.schemawizard.core.migration.factory.impl.PostgreSqlColumnTypeFactory;
import org.schemawizard.core.migration.operation.Operation;
import org.schemawizard.core.migration.operation.resolver.OperationResolver;
import org.schemawizard.core.migration.service.ColumnNamingStrategyService;
import org.schemawizard.core.migration.service.OperationResolverService;
import org.schemawizard.core.migration.service.OperationService;
import org.schemawizard.core.migration.service.impl.OperationResolverServiceImpl;
import org.schemawizard.core.migration.service.impl.OperationServiceImpl;
import org.schemawizard.core.migration.service.impl.SnakeCaseColumnNamingStrategyService;
import org.schemawizard.core.model.ConfigurationProperties;
import org.schemawizard.core.property.service.ConfigurationPropertiesService;
import org.schemawizard.core.property.service.PropertyParser;
import org.schemawizard.core.property.service.impl.CamelCasePropertyUtils;
import org.schemawizard.core.property.service.impl.ConfigurationPropertiesServiceImpl;
import org.schemawizard.core.property.service.impl.PropertyParserImpl;
import org.schemawizard.core.runner.MigrationRunner;
import org.schemawizard.core.runner.impl.MigrationRunnerImpl;
import org.yaml.snakeyaml.introspector.PropertyUtils;

import java.io.File;
import java.util.AbstractMap;
import java.util.Map;

public class SchemaWizardBuilder {
    private final PropertiesResolver propertiesResolver;
    private ClassLoader classLoader;

    private static final String SW_BASE_PACKAGE_NAME = "org.schemawizard.core";

    private SchemaWizardBuilder(
            PropertiesResolver propertiesResolver
    ) {
        this.propertiesResolver = propertiesResolver;
        this.classLoader = Thread.currentThread().getContextClassLoader();
    }

    public static SchemaWizardBuilder init() {
        return new SchemaWizardBuilder(new DefaultPropertiesResolver());
    }

    public static SchemaWizardBuilder init(String location) {
        return new SchemaWizardBuilder(new LocationPropertiesResolver(location));
    }

    public static SchemaWizardBuilder init(File file) {
        return new SchemaWizardBuilder(new FilePropertiesResolver(file));
    }

    public SchemaWizardBuilder classLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
        return this;
    }

    public SchemaWizard build() {
        DiContainer container = initDiContainerWithPropertyServices();

        ConfigurationPropertiesService propertiesService = container.resolve(ConfigurationPropertiesService.class);
        ConfigurationProperties properties = propertiesResolver.resolve(propertiesService);
        DatabaseProvider provider = properties.getDatabaseProvider();

        container.register(ConfigurationProperties.class, properties);
        container.register(OperationService.class, OperationServiceImpl.class);
        container.register(ColumnTypeFactory.class, PostgreSqlColumnTypeFactory.class);
        container.register(ColumnTypeFactory.class, OracleColumnTypeFactory.class);
        container.register(DowngradeFactory.class, DowngradeFactoryImpl.class);
        container.register(TransactionService.class, TransactionServiceImpl.class);
        container.register(HistoryTableQueryFactory.class, getHistoryTableQueryFactoryClass(provider));
        container.register(ColumnNamingStrategyService.class, getColumnNamingStrategyServiceClass(properties.getColumnNamingStrategy()));
        container.register(AppliedMigrationsService.class, AppliedMigrationsServiceImpl.class);
        container.register(DeclaredMigrationService.class, ClassesDeclaredMigrationService.class);
        container.register(ConnectionHolder.class, ConnectionHolder.class);
        container.register(HistoryTable.class, HistoryTableImpl.class);
        container.register(MigrationAnalyzer.class, MigrationAnalyzerImpl.class);
        container.register(MigrationRunner.class, MigrationRunnerImpl.class);
        container.register(OperationResolverService.class, OperationResolverServiceImpl.class);
        container.register(DriverLoader.class, DriverLoaderImpl.class);
        container.register(ReflectionsFactory.class, ReflectionsFactoryImpl.class);
        container.register(SchemaWizard.class, SchemaWizard.class);
        if (properties.isLogGeneratedSql()) {
            container.register(QueryGeneratedCallback.class, QueryLoggerCallback.class);
        }

        ReflectionsFactory factory = container.resolve(ReflectionsFactory.class);

        Reflections basePackageReflections = factory.newInstance(SW_BASE_PACKAGE_NAME);
        registerResolvers(basePackageReflections, container, provider);
        if (properties.getExtensionPackage() != null) {
            Reflections extensionPackageReflections = factory.newInstance(properties.getExtensionPackage());
            registerResolvers(extensionPackageReflections, container, provider);
            extensionPackageReflections.getSubTypesOf(BeforeQueryExecutionCallback.class)
                    .forEach(callback -> container.register(BeforeQueryExecutionCallback.class, callback));
            extensionPackageReflections.getSubTypesOf(AfterQueryExecutionCallback.class)
                    .forEach(callback -> container.register(AfterQueryExecutionCallback.class, callback));
        }

        return container.resolve(SchemaWizard.class);
    }

    private DiContainer initDiContainerWithPropertyServices() {
        DiContainer container = new DiContainer();

        container.register(ClassLoader.class, classLoader);
        container.register(PropertyUtils.class, CamelCasePropertyUtils.class);
        container.register(PropertyParser.class, PropertyParserImpl.class);
        container.register(ConfigurationPropertiesService.class, ConfigurationPropertiesServiceImpl.class);

        return container;
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

    private static Class<? extends ColumnNamingStrategyService> getColumnNamingStrategyServiceClass(ColumnNamingStrategy strategy) {
        if (strategy == null) {
            throw new InvalidConfigurationException(
                    ErrorMessage.NO_COLUMN_NAMING_STRATEGY_FOUND);
        }
        if (strategy == ColumnNamingStrategy.SNAKE_CASE) {
            return SnakeCaseColumnNamingStrategyService.class;
        }
        throw new InvalidConfigurationException(
                String.format(
                        ErrorMessage.NO_COLUMN_NAMING_STRATEGY_FOUND_FORMAT,
                        strategy));
    }

    private interface PropertiesResolver {
        ConfigurationProperties resolve(ConfigurationPropertiesService service);
    }

    private static class DefaultPropertiesResolver implements PropertiesResolver {
        @Override
        public ConfigurationProperties resolve(ConfigurationPropertiesService service) {
            return service.getProperties();
        }
    }

    private static class LocationPropertiesResolver implements PropertiesResolver {
        private final String location;

        private LocationPropertiesResolver(String location) {
            this.location = location;
        }

        @Override
        public ConfigurationProperties resolve(ConfigurationPropertiesService service) {
            return service.getProperties(location);
        }
    }

    private static class FilePropertiesResolver implements PropertiesResolver {
        private final File file;

        private FilePropertiesResolver(File file) {
            this.file = file;
        }

        @Override
        public ConfigurationProperties resolve(ConfigurationPropertiesService service) {
            return service.getProperties(file);
        }
    }
}
