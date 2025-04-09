package io.github.andriybosik.schemawizard.core.migration.builder.operation;

import io.github.andriybosik.schemawizard.core.exception.InvalidMigrationDefinitionException;
import io.github.andriybosik.schemawizard.core.metadata.ErrorMessage;
import io.github.andriybosik.schemawizard.core.migration.builder.column.ColumnBuilder;
import io.github.andriybosik.schemawizard.core.migration.factory.ColumnBuilderFactory;
import io.github.andriybosik.schemawizard.core.migration.metadata.ReferentialAction;
import io.github.andriybosik.schemawizard.core.migration.model.Pair;
import io.github.andriybosik.schemawizard.core.migration.operation.AddCheckOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.AddColumnOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.AddForeignKeyOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.AddPrimaryKeyOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.AddUniqueOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.CreateTableOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.Operation;
import io.github.andriybosik.schemawizard.core.utils.ReflectionUtils;
import io.github.andriybosik.schemawizard.core.utils.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
    private final List<ForeignKeyDefinition> foreignKeyDefinitions = new ArrayList<>();
    private final List<UniqueDefinition<T>> uniqueDefinitions = new ArrayList<>();
    private final List<CheckDefinition> checkDefinitions = new ArrayList<>();

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
            throw new InvalidMigrationDefinitionException(ErrorMessage.DUPLICATE_PRIMARY_KEY_DEFINITION);
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
            throw new InvalidMigrationDefinitionException(ErrorMessage.DUPLICATE_PRIMARY_KEY_DEFINITION);
        }
        this.primaryKeyDefinition = new PrimaryKeyDefinition<>(name, primaryKeyFunc);
        return this;
    }

    public CreateTable<T> foreignKey(Consumer<SingleForeignKey<T>> foreignKeyConsumer) {
        return foreignKey(null, foreignKeyConsumer);
    }

    public CreateTable<T> compositeForeignKey(Consumer<CompositeForeignKey<T>> foreignKeyConsumer) {
        return compositeForeignKey(null, foreignKeyConsumer);
    }

    public CreateTable<T> foreignKey(String name, Consumer<SingleForeignKey<T>> foreignKeyConsumer) {
        SingleForeignKey<T> builder = SingleForeignKey.builder(columnsDefinitor)
                .name(name);
        foreignKeyConsumer.accept(builder);
        foreignKeyDefinitions.add(builder.build());
        return this;
    }

    public CreateTable<T> compositeForeignKey(String name, Consumer<CompositeForeignKey<T>> foreignKeyConsumer) {
        CompositeForeignKey<T> builder = CompositeForeignKey.builder(columnsDefinitor)
                .name(name);
        foreignKeyConsumer.accept(builder);
        foreignKeyDefinitions.add(builder.build());
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

    public CreateTable<T> check(String condition) {
        return check(null, condition);
    }

    public CreateTable<T> check(String name, String condition) {
        this.checkDefinitions.add(new CheckDefinition(name, condition));
        return this;
    }

    @Override
    public Operation build() {
        List<Pair<ColumnBuilder, AddColumnOperation>> columnBuilders = Arrays.stream(columnsDefinitor.getClass().getDeclaredFields())
                .filter(field -> field.getType().isAssignableFrom(ColumnBuilder.class))
                .filter(this::isAccessible)
                .map(field -> ReflectionUtils.setAccessible(field, true))
                .map(field -> Pair.of(
                        ReflectionUtils.<ColumnBuilder>getValue(field, columnsDefinitor),
                        this.getOperationValue(field)))
                .collect(Collectors.toList());

        return new CreateTableOperation(
                schema,
                table,
                ifNotExists,
                primaryKeyDefinition == null ? null : new AddPrimaryKeyOperation(
                        schema,
                        table,
                        primaryKeyDefinition.getName(),
                        primaryKeyDefinition.getFunc().apply(columnsDefinitor).stream()
                                .map(columnBuilder -> findOperation(columnBuilder, columnBuilders))
                                .map(AddColumnOperation::getName)
                                .toArray(String[]::new)),
                columnBuilders.stream()
                        .map(Pair::getRight)
                        .sorted(Comparator.comparing(AddColumnOperation::getName))
                        .collect(Collectors.toList()),
                foreignKeyDefinitions.stream()
                        .map(definition -> mapToForeignKeyOperation(definition, columnBuilders))
                        .collect(Collectors.toList()),
                uniqueDefinitions.stream()
                        .map(definition -> new AddUniqueOperation(
                                schema,
                                table,
                                definition.getName(),
                                definition.getFunc().apply(columnsDefinitor).stream()
                                        .map(columnBuilder -> findOperation(columnBuilder, columnBuilders))
                                        .map(AddColumnOperation::getName)
                                        .toArray(String[]::new)))
                        .collect(Collectors.toList()),
                checkDefinitions.stream()
                        .map(definition -> new AddCheckOperation(
                                schema,
                                table,
                                definition.getName(),
                                definition.getCondition()))
                        .collect(Collectors.toList()));
    }

    private AddColumnOperation findOperation(ColumnBuilder columnBuilder, List<Pair<ColumnBuilder, AddColumnOperation>> columnBuilders) {
        return columnBuilders.stream()
                .filter(pair -> pair.getLeft() == columnBuilder)
                .findFirst()
                .map(Pair::getRight)
                .orElseThrow(() -> new InvalidMigrationDefinitionException(
                        String.format(
                                ErrorMessage.COLUMN_DEFINITION_NOT_FOUND_FOR_TABLE_FORMAT,
                                table)));
    }

    private boolean isAccessible(Field field) {
        return Modifier.isPublic(field.getModifiers())
                || (!Modifier.isPrivate(field.getModifiers()) && !Modifier.isProtected(field.getModifiers()));
    }

    private AddColumnOperation getOperationValue(Field field) {
        ReflectionUtils.setAccessible(field, true);
        ColumnBuilder builder = ReflectionUtils.getValue(field, columnsDefinitor);
        AddColumnOperation operation = builder.build();
        if (!StringUtils.isBlank(operation.getName())) {
            return operation;
        }
        return builder
                .name(field.getName())
                .build();
    }

    private AddForeignKeyOperation mapToForeignKeyOperation(ForeignKeyDefinition definition, List<Pair<ColumnBuilder, AddColumnOperation>> columnBuilders) {
        return new AddForeignKeyOperation(
                schema,
                table,
                Arrays.stream(definition.getColumns())
                        .map(columnBuilder -> findOperation(columnBuilder, columnBuilders))
                        .map(AddColumnOperation::getName)
                        .toArray(String[]::new),
                definition.getName(),
                definition.getForeignSchema(),
                definition.getForeignTable(),
                definition.getForeignColumns(),
                definition.getOnUpdate(),
                definition.getOnDelete());
    }

    public static class SingleForeignKey<T> {
        private final T columnDefinitor;
        private String name;
        private ColumnBuilder column;
        private String foreignSchema;
        private String foreignTable;
        private String foreignColumn;
        private ReferentialAction onUpdate = ReferentialAction.NO_ACTION;
        private ReferentialAction onDelete = ReferentialAction.NO_ACTION;

        private SingleForeignKey(T columnDefinitor) {
            this.columnDefinitor = columnDefinitor;
        }

        public static <T> SingleForeignKey<T> builder(T columnDefinitor) {
            return new SingleForeignKey<>(columnDefinitor);
        }

        public SingleForeignKey<T> name(String name) {
            this.name = name;
            return this;
        }

        public SingleForeignKey<T> column(Function<T, ColumnBuilder> columnFunction) {
            this.column = columnFunction.apply(columnDefinitor);
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

        private ForeignKeyDefinition build() {
            return new ForeignKeyDefinition(
                    name,
                    new ColumnBuilder[]{column},
                    foreignSchema,
                    foreignTable,
                    new String[]{foreignColumn},
                    onUpdate,
                    onDelete
            );
        }
    }

    public static class CompositeForeignKey<T> {
        private final T columnDefinitor;
        private String name;
        private ColumnBuilder[] columns;
        private String foreignSchema;
        private String foreignTable;
        private String[] foreignColumns;
        private ReferentialAction onUpdate = ReferentialAction.NO_ACTION;
        private ReferentialAction onDelete = ReferentialAction.NO_ACTION;

        private CompositeForeignKey(T columnDefinitor) {
            this.columnDefinitor = columnDefinitor;
        }

        public static <T> CompositeForeignKey<T> builder(T columnDefinitor) {
            return new CompositeForeignKey<>(columnDefinitor);
        }

        public CompositeForeignKey<T> name(String name) {
            this.name = name;
            return this;
        }

        public CompositeForeignKey<T> columns(Function<T, List<ColumnBuilder>> columnsFunction) {
            this.columns = columnsFunction.apply(columnDefinitor).toArray(ColumnBuilder[]::new);
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

        private ForeignKeyDefinition build() {
            return new ForeignKeyDefinition(
                    name,
                    columns,
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

    private static class ForeignKeyDefinition {
        private final String name;
        private final ColumnBuilder[] columns;
        private final String foreignSchema;
        private final String foreignTable;
        private final String[] foreignColumns;
        private final ReferentialAction onUpdate;
        private final ReferentialAction onDelete;

        public ForeignKeyDefinition(
                String name,
                ColumnBuilder[] columns,
                String foreignSchema,
                String foreignTable,
                String[] foreignColumns,
                ReferentialAction onUpdate,
                ReferentialAction onDelete
        ) {
            this.name = name;
            this.columns = columns;
            this.foreignSchema = foreignSchema;
            this.foreignTable = foreignTable;
            this.foreignColumns = foreignColumns;
            this.onUpdate = onUpdate;
            this.onDelete = onDelete;
        }

        public String getName() {
            return name;
        }

        public ColumnBuilder[] getColumns() {
            return columns;
        }

        public String getForeignSchema() {
            return foreignSchema;
        }

        public String getForeignTable() {
            return foreignTable;
        }

        public String[] getForeignColumns() {
            return foreignColumns;
        }

        public ReferentialAction getOnUpdate() {
            return onUpdate;
        }

        public ReferentialAction getOnDelete() {
            return onDelete;
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

    private static class CheckDefinition {
        private final String name;
        private final String condition;

        private CheckDefinition(String name, String condition) {
            this.name = name;
            this.condition = condition;
        }

        public String getName() {
            return name;
        }

        public String getCondition() {
            return condition;
        }
    }
}
