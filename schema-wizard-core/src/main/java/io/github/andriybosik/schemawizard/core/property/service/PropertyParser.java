package io.github.andriybosik.schemawizard.core.property.service;

public interface PropertyParser {
    String parseStringValue(String propertyValue);

    boolean parseBooleanValue(String value);

    Integer parseIntegerValue(String value);
}
