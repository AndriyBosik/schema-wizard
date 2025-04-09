package io.github.andriybosik.schemawizard.core.property.service.impl;

import io.github.andriybosik.schemawizard.core.exception.InvalidConfigurationException;
import io.github.andriybosik.schemawizard.core.exception.InvalidConfigurationPropertiesLocation;
import io.github.andriybosik.schemawizard.core.metadata.ColumnNamingStrategy;
import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;
import io.github.andriybosik.schemawizard.core.metadata.ErrorMessage;
import io.github.andriybosik.schemawizard.core.model.ConfigurationProperties;
import io.github.andriybosik.schemawizard.core.model.defaults.Defaults;
import io.github.andriybosik.schemawizard.core.model.defaults.Text;
import io.github.andriybosik.schemawizard.core.property.model.YamlContext;
import io.github.andriybosik.schemawizard.core.property.service.ConfigurationPropertiesService;
import io.github.andriybosik.schemawizard.core.property.service.PropertyParser;
import io.github.andriybosik.schemawizard.core.utils.IOUtils;
import io.github.andriybosik.schemawizard.core.utils.StringUtils;
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
    private final static String DEFAULT_LOCATION = "schema-wizard.yml";

    private final PropertyUtils propertyUtils;
    private final PropertyParser propertyParser;
    private final ClassLoader classLoader;

    public ConfigurationPropertiesServiceImpl(
            PropertyUtils propertyUtils,
            PropertyParser propertyParser,
            ClassLoader classLoader
    ) {
        this.propertyUtils = propertyUtils;
        this.propertyParser = propertyParser;
        this.classLoader = classLoader;
    }

    @Override
    public ConfigurationProperties getProperties() {
        return getProperties(DEFAULT_LOCATION);
    }

    @Override
    public ConfigurationProperties getProperties(String location) {
        URL resource = classLoader.getResource(location);
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
        } else if (lowerCaseConnectionUrl.startsWith("jdbc:mysql:")) {
            return DatabaseProvider.MYSQL;
        }
        throw new InvalidConfigurationException(String.format(
                ErrorMessage.INVALID_DATABASE_PROVIDER_FORMAT,
                connectionUrl));
    }
}
