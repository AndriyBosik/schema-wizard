package org.schemawizard.core.property.service;

public interface PropertyParser {
    String parseStringValue(String propertyValue);

    boolean parseBooleanValue(String value);
}
