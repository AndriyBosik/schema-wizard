package org.schemawizard.core.property.service.impl;

import org.schemawizard.core.exception.InvalidEnvironmentVariable;
import org.schemawizard.core.metadata.ErrorMessage;
import org.schemawizard.core.property.service.PropertyParser;
import org.schemawizard.core.utils.StringUtils;

public class PropertyParserImpl implements PropertyParser {
    private final static String ENVIRONMENT_START_SUBSTRING = "${";
    private final static String ENVIRONMENT_FINISH_SUBSTRING = "}";
    private final static String SEMICOLON = ":";

    @Override
    public String parseStringValue(String propertyValue) {
        if (StringUtils.isBlank(propertyValue)) {
            return null;
        }
        int searchFrom = 0;
        int envPatternStartFrom;
        int plainValueStartFrom = 0;
        StringBuilder value = new StringBuilder();
        while ((envPatternStartFrom = propertyValue.indexOf(ENVIRONMENT_START_SUBSTRING, searchFrom)) > -1) {
            int envValueStartFrom = envPatternStartFrom + ENVIRONMENT_START_SUBSTRING.length();
            int closingCurlyBraceIndex = findBalancedBrace(propertyValue, envValueStartFrom);
            if (closingCurlyBraceIndex < 0) {
                throw new InvalidEnvironmentVariable(
                        String.format(ErrorMessage.INVALID_PROPERTY_VALUE_BRACKETS_NOT_BALANCED_FORMAT, propertyValue));
            }
            searchFrom = closingCurlyBraceIndex;
            if (envPatternStartFrom > plainValueStartFrom) {
                value.append(propertyValue, plainValueStartFrom, envPatternStartFrom);
            }
            value.append(parseEnvironmentValue(propertyValue.substring(envValueStartFrom, closingCurlyBraceIndex)));
            plainValueStartFrom = closingCurlyBraceIndex + 1;
        }
        int propertyValueLastIndex = propertyValue.length() - 1;
        if (propertyValueLastIndex > plainValueStartFrom) {
            value.append(propertyValue, plainValueStartFrom, propertyValue.length());
        }
        return value.toString();
    }

    @Override
    public boolean parseBooleanValue(String value) {
        String stringValue = parseStringValue(value);
        return Boolean.parseBoolean(stringValue);
    }

    @Override
    public Integer parseLongValue(String value) {
        String stringValue = parseStringValue(value);
        return Integer.parseInt(stringValue);
    }

    private int findBalancedBrace(String propertyValue, int envStart) {
        int notBalancedBraces = 1;
        for (int i = envStart; i < propertyValue.length(); i++) {
            char symbol = propertyValue.charAt(i);
            switch (symbol) {
                case '{':
                    notBalancedBraces++;
                    break;
                case '}':
                    notBalancedBraces--;
                    break;
            }
            if (notBalancedBraces == 0) {
                return i;
            }
        }
        return -1;
    }

    private String parseEnvironmentValue(String rawValue) {
        rawValue = StringUtils.strip(rawValue);
        if (rawValue == null) {
            throw new InvalidEnvironmentVariable(ErrorMessage.NULL_PROPERTY_VALUE);
        }
        int semicolonIndex = rawValue.indexOf(SEMICOLON);
        if (semicolonIndex == 0) {
            throw new InvalidEnvironmentVariable(
                    String.format(ErrorMessage.INVALID_ENVIRONMENT_VALUE_FORMAT, rawValue));
        }
        boolean semicolonPresent = semicolonIndex >= 1;
        String envName = rawValue.substring(0, semicolonPresent ? semicolonIndex : rawValue.length());
        String defaultValue = semicolonPresent ? rawValue.substring(semicolonIndex + 1) : null;
        String systemEnvValue = StringUtils.strip(System.getenv(envName));
        boolean blankSystemEnvValue = StringUtils.isBlank(systemEnvValue);
        if (blankSystemEnvValue && defaultValue == null) {
            throw new InvalidEnvironmentVariable(ErrorMessage.NON_EXISTENT_ENVIRONMENT);
        }
        return !blankSystemEnvValue ? systemEnvValue : defaultValue;
    }
}
