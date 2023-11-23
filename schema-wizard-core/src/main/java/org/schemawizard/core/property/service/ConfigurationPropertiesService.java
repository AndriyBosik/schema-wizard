package org.schemawizard.core.property.service;

import org.schemawizard.core.model.ConfigurationProperties;

public interface ConfigurationPropertiesService {
    ConfigurationProperties getProperties();

    ConfigurationProperties getProperties(String location);
}
