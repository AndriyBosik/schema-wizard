package io.github.andriybosik.schemawizard.core.dao.impl;

import io.github.andriybosik.schemawizard.core.dao.DriverLoader;
import io.github.andriybosik.schemawizard.core.exception.DriverInstantiationException;
import io.github.andriybosik.schemawizard.core.exception.InvalidConfigurationException;
import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;
import io.github.andriybosik.schemawizard.core.metadata.ErrorMessage;
import io.github.andriybosik.schemawizard.core.utils.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.sql.Driver;

public class DriverLoaderImpl implements DriverLoader {
    private final ClassLoader classLoader;

    public DriverLoaderImpl(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public Driver load(DatabaseProvider databaseProvider) {
        String driverClassName = getDriverClassName(databaseProvider);
        try {
            return (Driver) Class.forName(driverClassName, true, classLoader).getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException exception) {
            throw new DriverInstantiationException(
                    String.format(
                            ErrorMessage.UNABLE_TO_INSTANTIATE_DATABASE_DRIVER_FORMAT,
                            databaseProvider),
                    exception);
        }
    }

    private String getDriverClassName(DatabaseProvider databaseProvider) {
        if (StringUtils.isBlank(databaseProvider.getDriver())) {
            throw new InvalidConfigurationException(
                    String.format(
                            ErrorMessage.UNABLE_TO_DETERMINE_DRIVER_CLASS_FOR_DATABASE_PROVIDER_FORMAT,
                            databaseProvider));
        }
        return databaseProvider.getDriver();
    }
}
