package io.github.andriybosik.schemawizard.core.exception;

import java.util.Stack;

public class CircularDependencyException extends RuntimeException {
    public CircularDependencyException(String message) {
        super(message);
    }

    public static CircularDependencyException fromDependencyStack(Stack<Class<?>> dependencyStack, int startFrom) {
        StringBuilder sb = new StringBuilder("Circular dependency detected:\n");
        for (int i = startFrom; i < dependencyStack.size(); i++) {
            sb.append(dependencyStack.get(i).getName());
            sb.append("\n     ↓\n");
        }
        sb.append(dependencyStack.get(startFrom).getName());
        return new CircularDependencyException(sb.toString());
    }
}
