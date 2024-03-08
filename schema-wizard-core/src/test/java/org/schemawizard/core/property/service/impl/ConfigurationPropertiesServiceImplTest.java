package org.schemawizard.core.property.service.impl;

import org.junit.jupiter.api.Test;
import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.model.ConfigurationProperties;
import org.schemawizard.core.model.defaults.Defaults;
import org.schemawizard.core.model.defaults.Text;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ConfigurationPropertiesServiceImplTest {

    private final ConfigurationPropertiesServiceImpl configurationPropertiesService
            = new ConfigurationPropertiesServiceImpl(new CamelCasePropertyUtils(), new PropertyParserImpl());

    @Test
    void propertiesServiceShouldReturnCorrectProperties() {
        ConfigurationProperties actualProperties = configurationPropertiesService.getProperties();
        ConfigurationProperties expectedProperties = new ConfigurationProperties(
                DatabaseProvider.POSTGRESQL,
                "context",
                "jdbc:postgresql://default:5432/changeMe?reWriteBatchedInserts=true",
                "postgres",
                "postgres",
                "org.schemawizard.core.db.migration",
                "org.schemawizard.core.migration.operation.resolver",
                false,
            new Defaults(new Text(31))
        );
        assertEquals(expectedProperties.getDatabaseProvider(), actualProperties.getDatabaseProvider());
        assertEquals(expectedProperties.getContext(), actualProperties.getContext());
        assertEquals(expectedProperties.getUsername(), actualProperties.getUsername());
        assertEquals(expectedProperties.getPassword(), actualProperties.getPassword());
        assertEquals(expectedProperties.getConnectionUrl(), actualProperties.getConnectionUrl());
        assertEquals(expectedProperties.getMigrationsPackage(), actualProperties.getMigrationsPackage());
        assertEquals(expectedProperties.getDefaults().getText().getDefaultLength(),
            actualProperties.getDefaults().getText().getDefaultLength());
        assertFalse(expectedProperties.isLogGeneratedSql());
    }
}
