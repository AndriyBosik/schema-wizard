package com.example.property.service.impl;

import com.example.exception.InvalidConfigurationPropertiesLocation;
import com.example.metadata.ErrorMessage;
import com.example.model.ConfigurationProperties;
import com.example.property.model.YamlContext;
import com.example.property.service.ConfigurationPropertiesService;
import com.example.property.service.PropertyParser;
import com.example.utils.IOUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.representer.Representer;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigurationPropertiesServiceImpl implements ConfigurationPropertiesService {
    private final static String DEFAULT_LOCATION = "application.yaml";

    private final PropertyUtils propertyUtils;
    private final PropertyParser propertyParser;

    public ConfigurationPropertiesServiceImpl(
            PropertyUtils propertyUtils,
            PropertyParser propertyParser
    ) {
        this.propertyUtils = propertyUtils;
        this.propertyParser = propertyParser;
    }

    @Override
    public ConfigurationProperties getProperties() {
        return getProperties(DEFAULT_LOCATION);
    }

    @Override
    public ConfigurationProperties getProperties(String location) {
        URL resource = getClass().getClassLoader().getResource(location);
        if (resource == null) {
            throw new InvalidConfigurationPropertiesLocation(
                    String.format(ErrorMessage.INVALID_PROPERTIES_LOCATION_FORMAT, location));
        }
        InputStream inputStream = IOUtils.getInputStream(resource.getFile());
        Yaml yaml = new Yaml(buildConstructor(), buildRepresenter());
        YamlContext context = yaml.loadAs(inputStream, YamlContext.class);
        return mapConfigurationProperties(context);
    }

    private Representer buildRepresenter() {
        Representer representer = new Representer(new DumperOptions());
        representer.getPropertyUtils().setSkipMissingProperties(true);
        return representer;
    }

    private Constructor buildConstructor() {
        Constructor constructor = new Constructor(YamlContext.class, new LoaderOptions());
        constructor.setPropertyUtils(propertyUtils);
        return constructor;
    }

    private ConfigurationProperties mapConfigurationProperties(YamlContext context) {
        List<String> migrationPackages = context.getSchema().getWizard().getMigration().getPackages().stream()
                .map(propertyParser::parseStringValue)
                .collect(Collectors.toList());

        List<ConfigurationProperties.LoggingItem> logging = context.getSchema().getWizard().getLogging().stream()
                .map(item -> new ConfigurationProperties.LoggingItem(
                        propertyParser.parseStringValue(item.getItem()),
                        propertyParser.parseStringValue(item.getLogLevel()),
                        propertyParser.parseBooleanValue(item.getEnabled())))
                .collect(Collectors.toList());

        return ConfigurationProperties.builder()
                .connectionUrl(propertyParser.parseStringValue(context.getSchema().getWizard().getDatabase().getConnectionUrl()))
                .username(propertyParser.parseStringValue(context.getSchema().getWizard().getDatabase().getUsername()))
                .password(propertyParser.parseStringValue(context.getSchema().getWizard().getDatabase().getPassword()))
                .packages(migrationPackages)
                .logging(logging)
                .build();
    }
}
