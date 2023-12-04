package org.schemawizard.core.migration.service.impl;

import org.schemawizard.core.callback.QueryGeneratedCallback;
import org.schemawizard.core.exception.ConflictDependencyException;
import org.schemawizard.core.metadata.ErrorMessage;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.Operation;
import org.schemawizard.core.migration.operation.resolver.OperationResolver;
import org.schemawizard.core.migration.service.OperationResolverService;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OperationResolverServiceImpl implements OperationResolverService {
    private final List<QueryGeneratedCallback> generatedCallbacks;
    private final Map<Type, OperationResolver> resolvers;

    public OperationResolverServiceImpl(
            List<QueryGeneratedCallback> generatedCallbacks,
            List<OperationResolver<?>> resolvers
    ) {
        this.generatedCallbacks = generatedCallbacks;
        this.resolvers = resolvers.stream()
                .map(item -> new AbstractMap.SimpleEntry<>(item.getClass().getGenericInterfaces(), item))
                .map(entry ->
                        Arrays.stream(entry.getKey())
                                .map(item -> (ParameterizedType) item)
                                .filter(item -> ((Class<?>) item.getRawType()).isAssignableFrom(OperationResolver.class))
                                .map(ParameterizedType::getActualTypeArguments)
                                .map(arguments -> arguments[0])
                                .map(item -> new AbstractMap.SimpleEntry<>(item, entry.getValue()))
                                .collect(Collectors.toList()))
                .flatMap(List::stream)
                .collect(Collectors.toMap(
                        AbstractMap.SimpleEntry::getKey,
                        AbstractMap.SimpleEntry::getValue,
                        (first, second) -> {
                            throw new ConflictDependencyException(String.format(
                                    ErrorMessage.MULTIPLE_OPERATION_RESOLVERS_FORMAT,
                                    first.getClass(),
                                    second.getClass()));
                        }));
    }

    @Override
    public MigrationInfo resolve(Operation operation) {
        OperationResolver resolver = this.resolvers.get(operation.getClass());
        MigrationInfo info = resolver.resolve(operation);
        for (QueryGeneratedCallback callback : generatedCallbacks) {
            callback.handle(info.getSql());
        }
        return info;
    }
}
