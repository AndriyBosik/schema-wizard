package io.github.andriybosik.schemawizard.core.property.service.impl;

import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.introspector.PropertyUtils;

public class CamelCasePropertyUtils extends PropertyUtils {
    private final static String CONFIGURATION_PROPERTY_NAME_PARSE_REGEXP = "-";

    @Override
    public Property getProperty(Class<?> type, String name) {
        return super.getProperty(type, camelize(name));
    }

    private String camelize(String name) {
        String[] parts = name.split(CONFIGURATION_PROPERTY_NAME_PARSE_REGEXP);
        StringBuilder camelCaseName = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            camelCaseName.append(parts[i].substring(0, 1).toUpperCase());
            camelCaseName.append(parts[i].substring(1));
        }
        return camelCaseName.toString();
    }
}
