package org.schemawizard.core.dao.impl;

import org.schemawizard.core.dao.DriverLoader;
import org.schemawizard.core.exception.DriverInstantiationException;
import org.schemawizard.core.exception.InvalidConfigurationException;
import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.metadata.ErrorMessage;
import org.schemawizard.core.utils.StringUtils;

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
