package io.github.andriybosik.schemawizard.core.starter;

import io.github.andriybosik.schemawizard.core.analyzer.HistoryTable;
import io.github.andriybosik.schemawizard.core.analyzer.MigrationAnalyzer;
import io.github.andriybosik.schemawizard.core.analyzer.factory.DowngradeFactory;
import io.github.andriybosik.schemawizard.core.analyzer.factory.ReflectionsFactory;
import io.github.andriybosik.schemawizard.core.analyzer.factory.impl.DowngradeFactoryImpl;
import io.github.andriybosik.schemawizard.core.analyzer.factory.impl.ReflectionsFactoryImpl;
import io.github.andriybosik.schemawizard.core.analyzer.impl.HistoryTableImpl;
import io.github.andriybosik.schemawizard.core.analyzer.impl.MigrationAnalyzerImpl;
import io.github.andriybosik.schemawizard.core.analyzer.service.AppliedMigrationsService;
import io.github.andriybosik.schemawizard.core.analyzer.service.DeclaredMigrationService;
import io.github.andriybosik.schemawizard.core.analyzer.service.impl.AppliedMigrationsServiceImpl;
import io.github.andriybosik.schemawizard.core.analyzer.service.impl.ClassesDeclaredMigrationService;
import io.github.andriybosik.schemawizard.core.callback.AfterQueryExecutionCallback;
import io.github.andriybosik.schemawizard.core.callback.BeforeQueryExecutionCallback;
import io.github.andriybosik.schemawizard.core.callback.QueryGeneratedCallback;
import io.github.andriybosik.schemawizard.core.callback.impl.QueryLoggerCallback;
import io.github.andriybosik.schemawizard.core.dao.ConnectionHolder;
import io.github.andriybosik.schemawizard.core.dao.DriverLoader;
import io.github.andriybosik.schemawizard.core.dao.HistoryTableQueryFactory;
import io.github.andriybosik.schemawizard.core.dao.TransactionService;
import io.github.andriybosik.schemawizard.core.dao.impl.DriverLoaderImpl;
import io.github.andriybosik.schemawizard.core.dao.impl.MySqlHistoryTableQueryFactory;
import io.github.andriybosik.schemawizard.core.dao.impl.OracleHistoryTableQueryFactory;
import io.github.andriybosik.schemawizard.core.dao.impl.PostgreSqlHistoryTableQueryFactory;
import io.github.andriybosik.schemawizard.core.dao.impl.SqlServerHistoryTableQueryFactory;
import io.github.andriybosik.schemawizard.core.dao.impl.TransactionServiceImpl;
import io.github.andriybosik.schemawizard.core.di.DiContainer;
import io.github.andriybosik.schemawizard.core.exception.InvalidConfigurationException;
import io.github.andriybosik.schemawizard.core.metadata.ColumnNamingStrategy;
import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;
import io.github.andriybosik.schemawizard.core.metadata.ErrorMessage;
import io.github.andriybosik.schemawizard.core.migration.annotation.Provider;
import io.github.andriybosik.schemawizard.core.migration.factory.ColumnTypeFactory;
import io.github.andriybosik.schemawizard.core.migration.factory.impl.MySqlColumnTypeFactory;
import io.github.andriybosik.schemawizard.core.migration.factory.impl.OracleColumnTypeFactory;
import io.github.andriybosik.schemawizard.core.migration.factory.impl.PostgreSqlColumnTypeFactory;
import io.github.andriybosik.schemawizard.core.migration.factory.impl.SqlServerColumnTypeFactory;
import io.github.andriybosik.schemawizard.core.migration.operation.Operation;
import io.github.andriybosik.schemawizard.core.migration.operation.resolver.OperationResolver;
import io.github.andriybosik.schemawizard.core.migration.service.ColumnNamingStrategyService;
import io.github.andriybosik.schemawizard.core.migration.service.OperationResolverService;
import io.github.andriybosik.schemawizard.core.migration.service.OperationService;
import io.github.andriybosik.schemawizard.core.migration.service.impl.OperationResolverServiceImpl;
import io.github.andriybosik.schemawizard.core.migration.service.impl.OperationServiceImpl;
import io.github.andriybosik.schemawizard.core.migration.service.impl.SnakeCaseColumnNamingStrategyService;
import io.github.andriybosik.schemawizard.core.model.ConfigurationProperties;
import io.github.andriybosik.schemawizard.core.property.model.YamlContext;
import io.github.andriybosik.schemawizard.core.property.service.ConfigurationPropertiesService;
import io.github.andriybosik.schemawizard.core.property.service.PropertyParser;
import io.github.andriybosik.schemawizard.core.property.service.impl.CamelCasePropertyUtils;
import io.github.andriybosik.schemawizard.core.property.service.impl.ConfigurationPropertiesServiceImpl;
import io.github.andriybosik.schemawizard.core.property.service.impl.PropertyParserImpl;
import io.github.andriybosik.schemawizard.core.runner.MigrationRunner;
import io.github.andriybosik.schemawizard.core.runner.impl.MigrationRunnerImpl;
import org.reflections.Reflections;
import org.yaml.snakeyaml.introspector.PropertyUtils;

import java.io.File;
import java.util.AbstractMap;
import java.util.Map;

public class SchemaWizardBuilder {
    private final PropertiesResolver propertiesResolver;
    private ClassLoader classLoader;

    private static final String SW_BASE_PACKAGE_NAME = "io.github.andriybosik.schemawizard.core";

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

    public static SchemaWizardBuilder init(YamlContext yamlContext) {
        return new SchemaWizardBuilder(new YamlContextPropertiesResolver(yamlContext));
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
        container.register(ColumnTypeFactory.class, MySqlColumnTypeFactory.class);
        container.register(ColumnTypeFactory.class, SqlServerColumnTypeFactory.class);
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
            return PostgreSqlHistoryTableQueryFactory.class;
        }
        if (provider == DatabaseProvider.ORACLE) {
            return OracleHistoryTableQueryFactory.class;
        }
        if (provider == DatabaseProvider.MYSQL) {
            return MySqlHistoryTableQueryFactory.class;
        }
        if (provider == DatabaseProvider.SQLSERVER) {
            return SqlServerHistoryTableQueryFactory.class;
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

    private static class YamlContextPropertiesResolver implements PropertiesResolver {
        private final YamlContext yamlContext;

        private YamlContextPropertiesResolver(YamlContext yamlContext) {
            this.yamlContext = yamlContext;
        }

        @Override
        public ConfigurationProperties resolve(ConfigurationPropertiesService service) {
            return service.getProperties(yamlContext);
        }
    }
}
