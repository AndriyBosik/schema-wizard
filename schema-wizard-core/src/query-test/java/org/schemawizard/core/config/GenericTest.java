package org.schemawizard.core.config;

import org.junit.jupiter.api.BeforeEach;
import org.reflections.Reflections;
import org.schemawizard.core.di.DiContainer;
import org.schemawizard.core.metadata.DatabaseProvider;
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
import org.schemawizard.core.model.defaults.Defaults;
import org.schemawizard.core.model.defaults.Text;
import org.schemawizard.core.utils.TestIOUtils;

import java.io.File;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class GenericTest {
    protected final OperationResolverService operationResolverService;
    protected final Set<DatabaseProvider> supportedProviders;

    protected GenericTest() {
        this(Set.of(DatabaseProvider.ORACLE, DatabaseProvider.POSTGRESQL));
    }

    protected GenericTest(DatabaseProvider provider) {
        this(Set.of(provider));
    }

    protected GenericTest(Set<DatabaseProvider> supportedProviders) {
        this.supportedProviders = supportedProviders;
        ConfigurationProperties properties = ConfigurationProperties.builder()
                .databaseProvider(TestContext.getProvider())
                .defaults(Defaults.builder()
                        .text(new Text(255))
                        .build())
                .build();

        DiContainer container = new DiContainer();

        container.register(ConfigurationProperties.class, properties);
        container.register(OperationService.class, OperationServiceImpl.class);
        container.register(ColumnTypeFactory.class, PostgreSqlColumnTypeFactory.class);
        container.register(ColumnTypeFactory.class, OracleColumnTypeFactory.class);

        Reflections basePackageReflections = new Reflections("org.schemawizard.core");
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
                        parserDatabaseProviderFromClass((Class<? extends OperationResolver<?>>) resolver)))
                .filter(pair -> pair.getValue() == provider || pair.getValue() == DatabaseProvider.MULTI)
                .map(Map.Entry::getKey);
    }

    private DatabaseProvider parserDatabaseProviderFromClass(Class<? extends OperationResolver<? extends Operation>> resolver) {
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
            default:
                throw new IllegalArgumentException("Unknown directory for database provider: " + TestContext.getProvider());
        }
    }
}
