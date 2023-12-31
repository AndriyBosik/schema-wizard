package org.schemawizard.core.property.service.impl;

import org.junit.jupiter.api.Test;
import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.model.ConfigurationProperties;

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
                "jdbc:postgresql://default:5432/changeMe?reWriteBatchedInserts=true",
                "postgres",
                "postgres",
                "org.schemawizard.core.db.migration",
                false
        );
        assertEquals(expectedProperties.getDatabaseProvider(), actualProperties.getDatabaseProvider());
        assertEquals(expectedProperties.getUsername(), actualProperties.getUsername());
        assertEquals(expectedProperties.getPassword(), actualProperties.getPassword());
        assertEquals(expectedProperties.getConnectionUrl(), actualProperties.getConnectionUrl());
        assertEquals(expectedProperties.getMigrationsPackage(), actualProperties.getMigrationsPackage());
        assertFalse(expectedProperties.isLogGeneratedSql());
    }
}
