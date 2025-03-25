package org.schemawizard.core.property.service;

import org.schemawizard.core.model.ConfigurationProperties;
import org.schemawizard.core.property.model.YamlContext;

import java.io.File;

import java.io.File;

public interface ConfigurationPropertiesService {
    ConfigurationProperties getProperties();

    ConfigurationProperties getProperties(String location);

    ConfigurationProperties getProperties(File file);

    ConfigurationProperties getProperties(YamlContext yamlContext);
}
