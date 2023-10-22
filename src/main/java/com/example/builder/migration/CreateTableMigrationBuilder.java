package com.example.builder.migration;

import com.example.builder.operation.*;
import com.example.builder.column.ColumnBuilder;
import com.example.builder.factory.ColumnBuilderFactory;
import com.example.exception.InvalidMigrationMetadataException;
import com.example.utils.ReflectionUtils;
import com.example.utils.StringUtils;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CreateTableMigrationBuilder<T> implements MigrationBuilder {
    private final String schema;
    private final String table;
    private boolean checkIfNotExists;
    private final T columnsDefinitor;
    private PrimaryKeyDefinition<T> primaryKeyDefinition;
    private final List<UniqueDefinition<T>> uniqueDefinitions = new ArrayList<>();

    private CreateTableMigrationBuilder(
            String schema,
            String table,
            T columnsDefinitor
    ) {
        this.schema = schema;
        this.table = table;
        this.columnsDefinitor = columnsDefinitor;
    }

    public static <T> CreateTableMigrationBuilder<T> builder(
            String schema,
            String table,
            Function<ColumnBuilderFactory, T> columnsFunction
    ) {
        ColumnBuilderFactory factory = new ColumnBuilderFactory(schema, table);
        return new CreateTableMigrationBuilder<>(schema, table, columnsFunction.apply(factory));
    }

    public static <T> CreateTableMigrationBuilder<T> builder(
            String table,
            Function<ColumnBuilderFactory, T> columnsFunction
    ) {
        ColumnBuilderFactory factory = new ColumnBuilderFactory(null, table);
        return new CreateTableMigrationBuilder<>(null, table, columnsFunction.apply(factory));
    }

    public CreateTableMigrationBuilder<T> checkIfNotExists(boolean checkIfNotExists) {
        this.checkIfNotExists = checkIfNotExists;
        return this;
    }

    public CreateTableMigrationBuilder<T> primaryKey(Function<T, ColumnBuilder> primaryKeyFunc) {
        Objects.requireNonNull(primaryKeyFunc);
        return this.compositePrimaryKey(null, obj -> List.of(primaryKeyFunc.apply(obj)));
    }

    public CreateTableMigrationBuilder<T> compositePrimaryKey(Function<T, List<ColumnBuilder>> primaryKeyFunc) {
        Objects.requireNonNull(primaryKeyFunc);
        return this.compositePrimaryKey(null, primaryKeyFunc);
    }

    public CreateTableMigrationBuilder<T> primaryKey(String name, Function<T, ColumnBuilder> primaryKeyFunc) {
        Objects.requireNonNull(primaryKeyFunc);
        return this.compositePrimaryKey(name, obj -> List.of(primaryKeyFunc.apply(obj)));
    }

    public CreateTableMigrationBuilder<T> compositePrimaryKey(String name, Function<T, List<ColumnBuilder>> primaryKeyFunc) {
        Objects.requireNonNull(primaryKeyFunc);
        if (this.primaryKeyDefinition != null) {
            throw new InvalidMigrationMetadataException("Duplicate primary key definition");
        }
        this.primaryKeyDefinition = new PrimaryKeyDefinition<>(name, primaryKeyFunc);
        return this;
    }

    public CreateTableMigrationBuilder<T> unique(Function<T, ColumnBuilder> uniqueFunc) {
        Objects.requireNonNull(uniqueFunc);
        return compositeUnique(null, obj -> List.of(uniqueFunc.apply(obj)));
    }

    public CreateTableMigrationBuilder<T> compositeUnique(Function<T, List<ColumnBuilder>> uniqueFunc) {
        Objects.requireNonNull(uniqueFunc);
        return compositeUnique(null, uniqueFunc);
    }

    public CreateTableMigrationBuilder<T> unique(String name, Function<T, ColumnBuilder> uniqueFunc) {
        Objects.requireNonNull(uniqueFunc);
        return compositeUnique(name, obj -> List.of(uniqueFunc.apply(obj)));
    }

    public CreateTableMigrationBuilder<T> compositeUnique(String name, Function<T, List<ColumnBuilder>> uniqueFunc) {
        Objects.requireNonNull(uniqueFunc);
        this.uniqueDefinitions.add(new UniqueDefinition<>(name, uniqueFunc));
        return this;
    }

    @Override
    public Operation build() {
        if (StringUtils.isBlank(table)) {
            throw new InvalidMigrationMetadataException("Table name must not be blank");
        }
        return new CreateTableOperation(
                schema,
                table,
                checkIfNotExists,
                new AddPrimaryKeyOperation(
                        schema,
                        table,
                        primaryKeyDefinition.getName(),
                        primaryKeyDefinition.getFunc().apply(columnsDefinitor).stream()
                                .map(ColumnBuilder::build)
                                .map(AddColumnOperation::getName)
                                .toArray(String[]::new)),
                Arrays.stream(columnsDefinitor.getClass().getDeclaredMethods())
                        .filter(method -> method.getParameterCount() == 0)
                        .filter(method -> method.getReturnType().isAssignableFrom(ColumnBuilder.class))
                        .filter(method -> Modifier.isPublic(method.getModifiers()))
                        .map(method -> ReflectionUtils.setAccessible(method, true))
                        .map(method -> ReflectionUtils.<ColumnBuilder>invokeMethod(method, columnsDefinitor))
                        .map(ColumnBuilder::build)
                        .collect(Collectors.toList()),
                uniqueDefinitions.stream()
                        .map(definition -> new AddUniqueConstraintOperation(
                                schema,
                                table,
                                definition.getName(),
                                definition.getFunc().apply(columnsDefinitor).stream()
                                        .map(ColumnBuilder::build)
                                        .map(AddColumnOperation::getName)
                                        .toArray(String[]::new)))
                        .collect(Collectors.toList()));
    }

    private static class PrimaryKeyDefinition<T> {
        private final String name;
        private final Function<T, List<ColumnBuilder>> func;

        private PrimaryKeyDefinition(String name, Function<T, List<ColumnBuilder>> func) {
            this.name = name;
            this.func = func;
        }

        public String getName() {
            return name;
        }

        public Function<T, List<ColumnBuilder>> getFunc() {
            return func;
        }
    }

    private static class UniqueDefinition<T> {
        private final String name;
        private final Function<T, List<ColumnBuilder>> func;

        private UniqueDefinition(String name, Function<T, List<ColumnBuilder>> func) {
            this.name = name;
            this.func = func;
        }

        public String getName() {
            return name;
        }

        public Function<T, List<ColumnBuilder>> getFunc() {
            return func;
        }
    }
}
