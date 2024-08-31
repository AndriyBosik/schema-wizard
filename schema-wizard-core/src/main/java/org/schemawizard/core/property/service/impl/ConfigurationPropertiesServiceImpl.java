package org.schemawizard.core.property.service.impl;

import org.schemawizard.core.exception.InvalidConfigurationException;
import org.schemawizard.core.exception.InvalidConfigurationPropertiesLocation;
import org.schemawizard.core.metadata.ColumnNamingStrategy;
import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.metadata.ErrorMessage;
import org.schemawizard.core.model.ConfigurationProperties;
import org.schemawizard.core.model.defaults.Defaults;
import org.schemawizard.core.model.defaults.Text;
import org.schemawizard.core.property.model.YamlContext;
import org.schemawizard.core.property.service.ConfigurationPropertiesService;
import org.schemawizard.core.property.service.PropertyParser;
import org.schemawizard.core.utils.IOUtils;
import org.schemawizard.core.utils.StringUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;

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
        return getProperties(IOUtils.getInputStream(resource.getFile()));
    }

    @Override
    public ConfigurationProperties getProperties(File file) {
        try {
            return getProperties(new FileInputStream(file));
        } catch (FileNotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public ConfigurationProperties getProperties(YamlContext context) {
        String connectionUrl = propertyParser.parseStringValue(context.getSchema().getWizard().getDatabase().getConnectionUrl());
        Text text = Text.builder()
                .defaultLength(propertyParser
                        .parseIntegerValue(context.getSchema().getWizard().getDefaults().getText().getMaxLength()))
                .build();
        Defaults defaults = Defaults.builder()
                .text(text)
                .build();

        String namingStrategy = Optional.ofNullable(context.getSchema().getWizard().getNamingStrategy())
                .map(YamlContext.Schema.Wizard.NamingStrategy::getColumn)
                .map(YamlContext.Schema.Wizard.NamingStrategy.Column::getType)
                .orElse(null);

        return ConfigurationProperties.builder()
                .databaseProvider(mapDatabaseProvider(connectionUrl))
                .context(propertyParser.parseStringValue(context.getSchema().getWizard().getContext()))
                .connectionUrl(connectionUrl)
                .username(propertyParser.parseStringValue(context.getSchema().getWizard().getDatabase().getUsername()))
                .password(propertyParser.parseStringValue(context.getSchema().getWizard().getDatabase().getPassword()))
                .migrationsPackage(propertyParser.parseStringValue(context.getSchema().getWizard().getMigration().getPackageName()))
                .extensionPackage(propertyParser.parseStringValue(context.getSchema().getWizard().getExtension().getPackageName()))
                .logGeneratedSql(propertyParser.parseBooleanValue(context.getSchema().getWizard().getLog().getSqlQuery()))
                .columnNamingStrategy(StringUtils.isBlank(namingStrategy) ? ColumnNamingStrategy.SNAKE_CASE : ColumnNamingStrategy.valueOf(namingStrategy))
                .defaults(defaults)
                .build();
    }

    private ConfigurationProperties getProperties(InputStream inputStream) {
        Yaml yaml = new Yaml(buildConstructor(), buildRepresenter());
        YamlContext context = yaml.loadAs(inputStream, YamlContext.class);
        return getProperties(context);
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

    private DatabaseProvider mapDatabaseProvider(String connectionUrl) {
        String lowerCaseConnectionUrl = connectionUrl.toLowerCase();
        if (lowerCaseConnectionUrl.startsWith("jdbc:postgresql:")) {
            return DatabaseProvider.POSTGRESQL;
        } else if (lowerCaseConnectionUrl.startsWith("jdbc:oracle:")) {
            return DatabaseProvider.ORACLE;
        }
        throw new InvalidConfigurationException(String.format(
                ErrorMessage.INVALID_DATABASE_PROVIDER_FORMAT,
                connectionUrl));
    }
}
