package io.github.andriybosik.schemawizard.core.config;

import io.github.andriybosik.schemawizard.core.config.extension.DisableForCondition;
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
import io.github.andriybosik.schemawizard.core.model.defaults.Defaults;
import io.github.andriybosik.schemawizard.core.model.defaults.Text;
import io.github.andriybosik.schemawizard.core.utils.TestIOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.reflections.Reflections;

import java.io.File;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@ExtendWith(DisableForCondition.class)
public class GenericTest {
    protected final OperationResolverService operationResolverService;
    protected final Set<DatabaseProvider> supportedProviders;

    protected GenericTest() {
        this(
                Set.of(
                        DatabaseProvider.ORACLE,
                        DatabaseProvider.POSTGRESQL,
                        DatabaseProvider.MYSQL,
                        DatabaseProvider.SQLSERVER));
    }

    protected GenericTest(DatabaseProvider provider) {
        this(Set.of(provider));
    }

    protected GenericTest(Set<DatabaseProvider> supportedProviders) {
        this.supportedProviders = supportedProviders;
        ConfigurationProperties properties = ConfigurationProperties.builder()
                .databaseProvider(TestContext.getProvider())
                .columnNamingStrategy(ColumnNamingStrategy.SNAKE_CASE)
                .defaults(Defaults.builder()
                        .text(new Text(255))
                        .build())
                .build();

        DiContainer container = new DiContainer();

        container.register(ClassLoader.class, Thread.currentThread().getContextClassLoader());
        container.register(ConfigurationProperties.class, properties);
        container.register(OperationService.class, OperationServiceImpl.class);
        container.register(ColumnTypeFactory.class, PostgreSqlColumnTypeFactory.class);
        container.register(ColumnTypeFactory.class, OracleColumnTypeFactory.class);
        container.register(ColumnTypeFactory.class, MySqlColumnTypeFactory.class);
        container.register(ColumnTypeFactory.class, SqlServerColumnTypeFactory.class);
        container.register(ColumnNamingStrategyService.class, getColumnNamingStrategyServiceClass(properties.getColumnNamingStrategy()));

        Reflections basePackageReflections = new Reflections("io.github.andriybosik.schemawizard.core");
        registerResolvers(basePackageReflections, TestContext.getProvider())
                .forEach(resolver -> container.register(OperationResolver.class, resolver));

        container.register(OperationResolverService.class, OperationResolverServiceImpl.class);

        this.operationResolverService = container.resolve(OperationResolverService.class);
    }

    @BeforeEach
    public void beforeEach() {
        assumeTrue(supportedProviders.contains(TestContext.getProvider()));
    }

    protected void assertQuery(String type, String actual) {
        String path = String.format("query/%s/%s.sql", type, getQueryFileName());
        File file = TestIOUtils.getResourceFile(path);
        String content = TestIOUtils.getContent(file);

        assertEquals(content, actual);
    }

    @SuppressWarnings("unchecked")
    private Stream<Class<? extends OperationResolver>> registerResolvers(Reflections reflections, DatabaseProvider provider) {
        return reflections.getSubTypesOf(OperationResolver.class).stream()
                .map(resolver -> new AbstractMap.SimpleEntry<>(
                        resolver,
                        parseDatabaseProviderFromClass((Class<? extends OperationResolver<?>>) resolver)))
                .filter(pair -> pair.getValue() == provider || pair.getValue() == DatabaseProvider.MULTI)
                .map(Map.Entry::getKey);
    }

    private DatabaseProvider parseDatabaseProviderFromClass(Class<? extends OperationResolver<? extends Operation>> resolver) {
        Provider annotation = resolver.getAnnotation(Provider.class);
        if (annotation == null) {
            return DatabaseProvider.MULTI;
        }
        return annotation.value();
    }

    private String getQueryFileName() {
        switch (TestContext.getProvider()) {
            case POSTGRESQL:
                return "postgresql";
            case ORACLE:
                return "oracle";
            case MYSQL:
                return "mysql";
            case SQLSERVER:
                return "sqlserver";
            default:
                throw new IllegalArgumentException("Unknown directory for database provider: " + TestContext.getProvider());
        }
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
}
