package io.github.andriybosik.schemawizard.core.property.service;

import io.github.andriybosik.schemawizard.core.model.ConfigurationProperties;
import io.github.andriybosik.schemawizard.core.property.model.YamlContext;

import java.io.File;

public interface ConfigurationPropertiesService {
    ConfigurationProperties getProperties();

    ConfigurationProperties getProperties(String location);

    ConfigurationProperties getProperties(File file);

    ConfigurationProperties getProperties(YamlContext yamlContext);
}
