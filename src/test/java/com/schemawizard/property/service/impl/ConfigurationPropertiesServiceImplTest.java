package com.schemawizard.property.service.impl;

import com.example.model.ConfigurationProperties;
import com.example.property.service.impl.CamelCasePropertyUtils;
import com.example.property.service.impl.ConfigurationPropertiesServiceImpl;
import com.example.property.service.impl.PropertyParserImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigurationPropertiesServiceImplTest {

    private final ConfigurationPropertiesServiceImpl configurationPropertiesService
            = new ConfigurationPropertiesServiceImpl(new CamelCasePropertyUtils(), new PropertyParserImpl());

    @Test
    void propertiesServiceShouldReturnCorrectProperties() {
        ConfigurationProperties actualProperties = configurationPropertiesService.getProperties();
        ConfigurationProperties expectedProperties = new ConfigurationProperties(
                "jdbc:postgresql://default:5432/changeMe?reWriteBatchedInserts=true",
                "postgres",
                "postgres",
                "com.example.db.migration",
                List.of(
                        new ConfigurationProperties.LoggingItem("upgrade", "TRACE", true),
                        new ConfigurationProperties.LoggingItem("downgrade", "WARN", false)
                )
        );
        assertEquals(expectedProperties.getUsername(), actualProperties.getUsername());
        assertEquals(expectedProperties.getPassword(), actualProperties.getPassword());
        assertEquals(expectedProperties.getConnectionUrl(), actualProperties.getConnectionUrl());
        assertEquals(expectedProperties.getLogging().size(), actualProperties.getLogging().size());
        for(int i = 0; i < expectedProperties.getLogging().size(); i++) {
            assertEquals(expectedProperties.getLogging().get(i).getItem(), actualProperties.getLogging().get(i).getItem());
            assertEquals(expectedProperties.getLogging().get(i).getLogLevel(), actualProperties.getLogging().get(i).getLogLevel());
            assertEquals(expectedProperties.getLogging().get(i).isEnabled(), actualProperties.getLogging().get(i).isEnabled());
        }
    }
}
