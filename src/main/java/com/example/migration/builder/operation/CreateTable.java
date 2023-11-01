package com.example.migration.builder.operation;

import com.example.exception.InvalidMigrationMetadataException;
import com.example.metadata.ErrorMessage;
import com.example.migration.builder.column.ColumnBuilder;
import com.example.migration.factory.ColumnBuilderFactory;
import com.example.migration.metadata.ReferentialAction;
import com.example.migration.operation.*;
import com.example.utils.ReflectionUtils;
import com.example.utils.StringUtils;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CreateTable<T> implements OperationBuilder {
    private final String schema;
    private final String table;
    private boolean ifNotExists;
    private final T columnsDefinitor;
    private PrimaryKeyDefinition<T> primaryKeyDefinition;
    private final List<AddForeignKeyOperation> foreignKeyOperations = new ArrayList<>();
    private final List<UniqueDefinition<T>> uniqueDefinitions = new ArrayList<>();

    private CreateTable(
            String schema,
            String table,
            T columnsDefinitor
    ) {
        this.schema = schema;
        this.table = table;
        this.ifNotExists = false;
        this.columnsDefinitor = columnsDefinitor;
    }

    public static <T> CreateTable<T> builder(
            String table,
            Function<ColumnBuilderFactory, T> columnsFunction
    ) {
        return CreateTable.builder(null, table, columnsFunction);
    }

    public static <T> CreateTable<T> builder(
            String schema,
            String table,
            Function<ColumnBuilderFactory, T> columnsFunction
    ) {
        if (StringUtils.isBlank(table)) {
            throw new InvalidMigrationMetadataException(ErrorMessage.DUPLICATE_PRIMARY_KEY_DEFINITION);
        }
        ColumnBuilderFactory factory = new ColumnBuilderFactory(schema, table);
        return new CreateTable<>(schema, table, columnsFunction.apply(factory));
    }

    public CreateTable<T> ifNotExists() {
        this.ifNotExists = true;
        return this;
    }

    public CreateTable<T> primaryKey(Function<T, ColumnBuilder> primaryKeyFunc) {
        Objects.requireNonNull(primaryKeyFunc);
        return this.compositePrimaryKey(null, obj -> List.of(primaryKeyFunc.apply(obj)));
    }

    public CreateTable<T> compositePrimaryKey(Function<T, List<ColumnBuilder>> primaryKeyFunc) {
        Objects.requireNonNull(primaryKeyFunc);
        return this.compositePrimaryKey(null, primaryKeyFunc);
    }

    public CreateTable<T> primaryKey(String name, Function<T, ColumnBuilder> primaryKeyFunc) {
        Objects.requireNonNull(primaryKeyFunc);
        return this.compositePrimaryKey(name, obj -> List.of(primaryKeyFunc.apply(obj)));
    }

    public CreateTable<T> compositePrimaryKey(String name, Function<T, List<ColumnBuilder>> primaryKeyFunc) {
        Objects.requireNonNull(primaryKeyFunc);
        if (this.primaryKeyDefinition != null) {
            throw new InvalidMigrationMetadataException("Duplicate primary key definition");
        }
        this.primaryKeyDefinition = new PrimaryKeyDefinition<>(name, primaryKeyFunc);
        return this;
    }

    public CreateTable<T> foreignKey(Consumer<SingleForeignKey<T>> foreignKeyConsumer) {
        SingleForeignKey<T> builder = SingleForeignKey.builder(schema, table, columnsDefinitor);
        foreignKeyConsumer.accept(builder);
        foreignKeyOperations.add(builder.build());
        return this;
    }

    public CreateTable<T> compositeForeignKey(Consumer<CompositeForeignKey<T>> foreignKeyConsumer) {
        CompositeForeignKey<T> builder = CompositeForeignKey.builder(schema, table, columnsDefinitor);
        foreignKeyConsumer.accept(builder);
        foreignKeyOperations.add(builder.build());
        return this;
    }

    public CreateTable<T> unique(Function<T, ColumnBuilder> uniqueFunc) {
        Objects.requireNonNull(uniqueFunc);
        return compositeUnique(null, obj -> List.of(uniqueFunc.apply(obj)));
    }

    public CreateTable<T> compositeUnique(Function<T, List<ColumnBuilder>> uniqueFunc) {
        Objects.requireNonNull(uniqueFunc);
        return compositeUnique(null, uniqueFunc);
    }

    public CreateTable<T> unique(String name, Function<T, ColumnBuilder> uniqueFunc) {
        Objects.requireNonNull(uniqueFunc);
        return compositeUnique(name, obj -> List.of(uniqueFunc.apply(obj)));
    }

    public CreateTable<T> compositeUnique(String name, Function<T, List<ColumnBuilder>> uniqueFunc) {
        Objects.requireNonNull(uniqueFunc);
        this.uniqueDefinitions.add(new UniqueDefinition<>(name, uniqueFunc));
        return this;
    }

    @Override
    public Operation build() {
        return new CreateTableOperation(
                schema,
                table,
                ifNotExists,
                primaryKeyDefinition == null ? null : new AddPrimaryKeyOperation(
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
                foreignKeyOperations,
                uniqueDefinitions.stream()
                        .map(definition -> new AddUniqueOperation(
                                schema,
                                table,
                                definition.getName(),
                                definition.getFunc().apply(columnsDefinitor).stream()
                                        .map(ColumnBuilder::build)
                                        .map(AddColumnOperation::getName)
                                        .toArray(String[]::new)))
                        .collect(Collectors.toList()));
    }

    public static class SingleForeignKey<T> {
        private final String schema;
        private final String table;
        private final T columnDefinitor;
        private String name;
        private String column;
        private String foreignSchema;
        private String foreignTable;
        private String foreignColumn;
        private ReferentialAction onUpdate = ReferentialAction.NO_ACTION;
        private ReferentialAction onDelete = ReferentialAction.NO_ACTION;

        private SingleForeignKey(String schema, String table, T columnDefinitor) {
            this.schema = schema;
            this.table = table;
            this.columnDefinitor = columnDefinitor;
        }

        public static <T> SingleForeignKey<T> builder(String schema, String table, T columnDefinitor) {
            return new SingleForeignKey<>(schema, table, columnDefinitor);
        }

        public SingleForeignKey<T> name(String name) {
            this.name = name;
            return this;
        }

        public SingleForeignKey<T> column(Function<T, ColumnBuilder> columnFunction) {
            this.column = columnFunction.apply(columnDefinitor).build().getName();
            return this;
        }

        public SingleForeignKey<T> foreignSchema(String foreignSchema) {
            this.foreignSchema = foreignSchema;
            return this;
        }

        public SingleForeignKey<T> foreignTable(String foreignTable) {
            this.foreignTable = foreignTable;
            return this;
        }

        public SingleForeignKey<T> foreignColumn(String foreignColumn) {
            this.foreignColumn = foreignColumn;
            return this;
        }

        public SingleForeignKey<T> onUpdate(ReferentialAction onUpdate) {
            this.onUpdate = onUpdate;
            return this;
        }

        public SingleForeignKey<T> onDelete(ReferentialAction onDelete) {
            this.onDelete = onDelete;
            return this;
        }

        public AddForeignKeyOperation build() {
            return new AddForeignKeyOperation(
                    schema,
                    table,
                    new String[]{column},
                    name,
                    foreignSchema,
                    foreignTable,
                    new String[]{foreignColumn},
                    onUpdate,
                    onDelete);
        }
    }

    public static class CompositeForeignKey<T> {
        private final String schema;
        private final String table;
        private final T columnDefinitor;
        private String name;
        private String[] columns;
        private String foreignSchema;
        private String foreignTable;
        private String[] foreignColumns;
        private ReferentialAction onUpdate = ReferentialAction.NO_ACTION;
        private ReferentialAction onDelete = ReferentialAction.NO_ACTION;

        private CompositeForeignKey(String schema, String table, T columnDefinitor) {
            this.schema = schema;
            this.table = table;
            this.columnDefinitor = columnDefinitor;
        }

        public static <T> CompositeForeignKey<T> builder(String schema, String table, T columnDefinitor) {
            return new CompositeForeignKey<>(schema, table, columnDefinitor);
        }

        public CompositeForeignKey<T> name(String name) {
            this.name = name;
            return this;
        }

        public CompositeForeignKey<T> columns(Function<T, List<ColumnBuilder>> columnsFunction) {
            this.columns = columnsFunction.apply(columnDefinitor).stream()
                    .map(ColumnBuilder::build)
                    .map(AddColumnOperation::getName)
                    .toArray(String[]::new);
            return this;
        }

        public CompositeForeignKey<T> foreignSchema(String foreignSchema) {
            this.foreignSchema = foreignSchema;
            return this;
        }

        public CompositeForeignKey<T> foreignTable(String foreignTable) {
            this.foreignTable = foreignTable;
            return this;
        }

        public CompositeForeignKey<T> foreignColumns(String... foreignColumns) {
            this.foreignColumns = foreignColumns;
            return this;
        }

        public CompositeForeignKey<T> onUpdate(ReferentialAction onUpdate) {
            this.onUpdate = onUpdate;
            return this;
        }

        public CompositeForeignKey<T> onDelete(ReferentialAction onDelete) {
            this.onDelete = onDelete;
            return this;
        }

        public AddForeignKeyOperation build() {
            return new AddForeignKeyOperation(
                    schema,
                    table,
                    columns,
                    name,
                    foreignSchema,
                    foreignTable,
                    foreignColumns,
                    onUpdate,
                    onDelete);
        }
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
