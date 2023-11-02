package com.example.starter;

import com.example.di.DiContainer;
import com.example.metadata.DatabaseProvider;
import com.example.migration.annotation.Provider;
import com.example.migration.operation.Operation;
import com.example.migration.operation.resolver.OperationResolver;
import com.example.migration.operation.resolver.custom.CustomDropTableOperationResolver;
import com.example.migration.operation.resolver.oracle.OracleAddColumnOperationResolver;
import com.example.migration.operation.resolver.oracle.OracleCreateTableOperationResolver;
import com.example.migration.operation.resolver.postgresql.PostgreSqlAddColumnOperationResolver;
import com.example.migration.operation.resolver.postgresql.PostgreSqlCreateTableOperationResolver;
import com.example.model.ConfigurationProperties;
import com.example.property.service.ConfigurationPropertiesService;
import com.example.property.service.PropertyParser;
import com.example.property.service.impl.CamelCasePropertyUtils;
import com.example.property.service.impl.ConfigurationPropertiesServiceImpl;
import com.example.property.service.impl.PropertyParserImpl;
import org.yaml.snakeyaml.introspector.PropertyUtils;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

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

        DatabaseProvider provider = parseDatabaseProvider(properties.getConnectionUrl());

        Stream.of(
                        PostgreSqlAddColumnOperationResolver.class,
                        PostgreSqlCreateTableOperationResolver.class,
                        OracleAddColumnOperationResolver.class,
                        OracleCreateTableOperationResolver.class,
                        CustomDropTableOperationResolver.class)
                .map(resolver -> new AbstractMap.SimpleEntry<>(resolver, parserDatabaseProviderFromClass(resolver)))
                .filter(pair -> pair.getValue() == provider || pair.getValue() == DatabaseProvider.DEFAULT)
                .map(Map.Entry::getKey)
                .forEach(resolver -> container.register(OperationResolver.class, resolver));

        container.register(SchemaWizard.class, SchemaWizard.class);

        return new SchemaWizardBuilder(container, properties);
    }

    public SchemaWizardBuilder registerResolver(Class<? extends OperationResolver<?>> resolverClass) {
        DatabaseProvider resolverProvider = parserDatabaseProviderFromClass(resolverClass);
        DatabaseProvider propertiesProvider = parseDatabaseProvider(properties.getConnectionUrl());
        if (Objects.equals(resolverProvider, propertiesProvider) || Objects.equals(resolverProvider, DatabaseProvider.DEFAULT)) {
            container.register(OperationResolver.class, resolverClass);
        }
        return this;
    }

    public SchemaWizard build() {
        return container.resolve(SchemaWizard.class);
    }

    private static DatabaseProvider parseDatabaseProvider(String connectionUrl) {
        if (connectionUrl == null) {
            return DatabaseProvider.DEFAULT;
        }
        if (connectionUrl.startsWith("jdbc:postgresql")) {
            return DatabaseProvider.POSTGRESQL;
        } else if (connectionUrl.startsWith("jdbc:oracle")) {
            return DatabaseProvider.ORACLE;
        }
        return DatabaseProvider.DEFAULT;
    }

    private static DatabaseProvider parserDatabaseProviderFromClass(Class<? extends OperationResolver<? extends Operation>> resolver) {
        Provider annotation = resolver.getAnnotation(Provider.class);
        if (annotation == null) {
            return DatabaseProvider.DEFAULT;
        }
        return annotation.value();
    }
}
