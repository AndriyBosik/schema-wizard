package com.example.property.service;

import com.example.model.ConfigurationProperties;

public interface ConfigurationPropertiesService {
    ConfigurationProperties getProperties();

    ConfigurationProperties getProperties(String location);
}
