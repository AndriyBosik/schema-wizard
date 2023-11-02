package com.example.starter;

import com.example.migration.operation.resolver.OperationResolver;

import java.util.List;

public class SchemaWizard {
    private final List<OperationResolver<?>> resolvers;

    public SchemaWizard(List<OperationResolver<?>> resolvers) {
        this.resolvers = resolvers;
    }

    public void up() {
        System.out.println(resolvers.size());
    }
}
