package com.example.property.service.impl;

import com.example.exception.InvalidEnvironmentVariable;
import com.example.property.service.PropertyParser;
import com.example.utils.StringUtils;

public class PropertyParserImpl implements PropertyParser {
    private final static String ENVIRONMENT_START_SUBSTRING = "${";

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
                throw new InvalidEnvironmentVariable("Invalid property value: '" + propertyValue + "'. Brackets are not balanced");
            }
            searchFrom = closingCurlyBraceIndex;
            if (envPatternStartFrom - plainValueStartFrom > 0) {
                value.append(propertyValue, plainValueStartFrom, envPatternStartFrom);
            }
            value.append(parseEnvironmentValue(propertyValue.substring(envValueStartFrom, closingCurlyBraceIndex)));
            plainValueStartFrom = closingCurlyBraceIndex + 1;
        }
        if (propertyValue.length() - 1 - plainValueStartFrom > 0) {
            value.append(propertyValue, plainValueStartFrom, propertyValue.length());
        }
        return value.toString();
    }

    @Override
    public boolean parseBooleanValue(String value) {
        String stringValue = parseStringValue(value);
        return Boolean.parseBoolean(stringValue);
    }

    private int findBalancedBrace(String propertyValue, int envStart) {
        int notBalancesBraces = 1;
        for (int i = envStart; i < propertyValue.length(); i++) {
            char symbol = propertyValue.charAt(i);
            if (symbol == '{') {
                notBalancesBraces++;
            } else if (symbol == '}') {
                notBalancesBraces--;
            }
            if (notBalancesBraces == 0) {
                return i;
            }
        }
        return -1;
    }

    private String parseEnvironmentValue(String rawValue) {
        rawValue = StringUtils.strip(rawValue);
        if (rawValue == null) {
            throw new InvalidEnvironmentVariable("Null property value");
        }
        int semicolonIndex = rawValue.indexOf(":");
        if (semicolonIndex == 0) {
            throw new InvalidEnvironmentVariable("Invalid value for environment variable: " + rawValue);
        }
        boolean semicolonPresent = semicolonIndex >= 1;
        String envName = rawValue.substring(0, semicolonPresent ? semicolonIndex : rawValue.length());
        String defaultValue = semicolonPresent ? rawValue.substring(semicolonIndex + 1) : null;
        String systemEnvValue = StringUtils.strip(System.getenv(envName));
        boolean blankSystemEnvValue = StringUtils.isBlank(systemEnvValue);
        if (blankSystemEnvValue && defaultValue == null) {
            throw new InvalidEnvironmentVariable("Environment value does not exist and no default value had been set");
        }
        return !blankSystemEnvValue ? systemEnvValue : defaultValue;
    }
}
