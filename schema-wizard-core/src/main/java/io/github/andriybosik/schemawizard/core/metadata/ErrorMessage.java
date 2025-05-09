package io.github.andriybosik.schemawizard.core.metadata;

public class ErrorMessage {
    public static final String UNABLE_TO_INSTANTIATE_DEPENDEE_NO_PRIMARY_CONSTRUCTOR_FORMAT =
            "Unable to instantiate dependee of class %s. No unique primary constructor was found. Consider using %s annotation only once!";
    public static final String UNABLE_TO_INSTANTIATE_DEPENDEE_NO_DEPENDENCY_INFORMATION_FORMAT =
            "Unable to instantiate dependee. Container has no information about dependency of %s class and with %s name";
    public static final String UNABLE_TO_INSTANTIATE_DEPENDEE_NO_CLASS_METADATA_FORMAT =
            "Unable to instantiate dependee. Container doesn't contain %s class metadata or contains more than one records";
    public static final String MULTIPLE_TYPE_INSTANCES_FORMAT =
            "More than one instance of %s was found. Use @Qualifier to define exact instance which you want to inject";
    public static final String DEPENDENCY_ALREADY_REGISTERED_FORMAT =
            "Dependency with name %s has been already registered";
    public static final String INVALID_PROPERTIES_LOCATION_FORMAT =
            "Properties by location %s do not exists";
    public static final String INVALID_PROPERTY_VALUE_BRACKETS_NOT_BALANCED_FORMAT =
            "Invalid property value: '%s'. Brackets are not balanced";
    public static final String NULL_PROPERTY_VALUE =
            "Null property value";
    public static final String INVALID_ENVIRONMENT_VALUE_FORMAT =
            "Invalid value for environment variable: %s";
    public static final String NON_EXISTENT_ENVIRONMENT =
            "Environment value does not exist and no default value had been set";
    public static final String DUPLICATE_PRIMARY_KEY_DEFINITION =
            "Duplicate primary key definition";
    public static final String BLANK_TABLE_NAME =
            "Table name must not be blank";
    public static final String EMPTY_NATIVE_QUERY_MIGRATION =
            "Native query migration is empty";
    public static final String NOT_EXISTENT_FILE_FORMAT =
            "File %s does not exist";
    public static final String INVALID_DATABASE_PROVIDER_FORMAT =
            "Invalid database provider for connection URL: %s";
    public static final String NO_HISTORY_TABLE_QUERY_FACTORY_FOUND_FORMAT =
            "No query factory found for database provider: %s";
    public static final String NO_COLUMN_NAMING_STRATEGY_FOUND =
            "No column naming strategy found";
    public static final String NO_COLUMN_NAMING_STRATEGY_FOUND_FORMAT =
            "No column naming strategy found for value: %s";
    public static final String COLUMN_DEFINITION_NOT_FOUND_FOR_TABLE_FORMAT =
            "Column definition not found for table: %s";
    public static final String MULTIPLE_OPERATION_RESOLVERS_FORMAT =
            "These resolvers use the same operation type: %s %s";
    public static final String UNABLE_TO_RESOLVE_DOWNGRADE_PARAMETERS_CLASS_TEMPLATE =
            "Unable to resolve downgrade parameters class: %s";
    public static final String MIGRATION_WITH_VERSION_WAS_NOT_FOUND_TEMPLATE =
            "Migration with version %s was not found";
    public static final String MIGRATIONS_WERE_NOT_FOUND = "No migrations were found";
    public static final String DOWNGRADE_CONTEXT_IS_INVALID_TEMPLATE =
            "The context %s is invalid - no migrations were found to downgrade";
    public static final String UNABLE_TO_LOCK_TABLE_TEMPLATE =
            "Unable to lock table %s";
    public static final String UNABLE_TO_INSTANTIATE_DATABASE_DRIVER_FORMAT =
            "Unable to instantiate driver for database provider: %s";
    public static final String UNABLE_TO_DETERMINE_DRIVER_CLASS_FOR_DATABASE_PROVIDER_FORMAT =
            "Unable to determine driver class for database provider: %s";
    public static final String EMPTY_COLUMN_NAME_FOR_OPERATION_FORMAT =
            "Empty column name for operation: %s";
    public static final String UNABLE_TO_DOWNGRADE_BY_COUNT_FORMAT =
            "Unable to downgrade migrations by count. %s applied migrations were found, but %s were requested to be downgraded";
    public static final String NEGATIVE_COUNT_TO_DOWNGRADE_FORMAT =
            "Downgrade count of migrations cannot be negative: %s";
    public static final String CONTEXT_VALUE_IS_BLANK_FORMAT =
            "Context value '%s' is blank";

    private ErrorMessage() {
    }
}
