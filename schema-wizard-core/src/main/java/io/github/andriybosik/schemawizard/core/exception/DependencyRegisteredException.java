package io.github.andriybosik.schemawizard.core.exception;

import io.github.andriybosik.schemawizard.core.metadata.ErrorMessage;

public class DependencyRegisteredException extends RuntimeException {
    public DependencyRegisteredException(String dependencyName) {
        super(String.format(ErrorMessage.DEPENDENCY_ALREADY_REGISTERED_FORMAT, dependencyName));
    }
}
