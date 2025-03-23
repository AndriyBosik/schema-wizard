package org.schemawizard.core.property.service;

import org.schemawizard.core.model.ConfigurationProperties;

import java.io.File;

public interface ConfigurationPropertiesService {
    ConfigurationProperties getProperties();

    ConfigurationProperties getProperties(String location);

    ConfigurationProperties getProperties(File file);
}
