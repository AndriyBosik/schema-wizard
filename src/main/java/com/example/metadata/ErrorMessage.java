package com.example.metadata;

public class ErrorMessage {
    public final static String UNABLE_TO_INSTANTIATE_DEPENDEE_NO_PRIMARY_CONSTRUCTOR_FORMAT =
            "Unable to instantiate dependee of class %s. No unique primary constructor was found. Consider using %s annotation only once!";
    public final static String UNABLE_TO_INSTANTIATE_DEPENDEE_NO_DEPENDENCY_INFORMATION_FORMAT =
            "Unable to instantiate dependee. Container has no information about dependency of %s class and with %s name";
    public final static String UNABLE_TO_INSTANTIATE_DEPENDEE_NO_CLASS_METADATA_FORMAT =
            "Unable to instantiate dependee. Container doesn't contain %s class metadata or contains more than one records";
    public final static String MULTIPLE_TYPE_INSTANCES_FORMAT =
            "More than one instance of %s was found. Use @Qualifier to define exact instance which you want to inject";
    public final static String DEPENDENCY_ALREADY_REGISTERED_FORMAT =
            "Dependency with name %s has been already registered";

    private ErrorMessage() {
    }
}
