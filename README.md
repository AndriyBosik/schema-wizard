# SchemaWizard - An Impressive Java-based Migrations Library

SchemaWizard is a powerful tool for managing database migrations easily.
Writing a database migration is just as easily as writing a simple Java class.
Library also supports cross-database compatibility - write once, run anywhere.

## Supported databases

1. PostgreSQL
2. Oracle
3. MySQL

## Installation

For Gradle-based project, include the following dependency to your project:

```groovy
implementation "io.github.andriybosik:schema-wizard-core:${version}"
```

For Maven-based project, include the following dependency to your project:

```xml
<dependency>
   <groupId>io.github.andriybosik</groupId>
   <artifactId>schema-wizard-core-preview</artifactId>
   <version>${version}</version>
</dependency>
```

## Configuration

SchemaWizard is configured via `schema-wizard.yml` file. You are able to configure the following properties:

| Property                                  | Description                                                                                                                                           | Type    | Default    | Example value                                  |
|-------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------|---------|------------|------------------------------------------------|
| schema.wizard.context                     | The context to use when running migrations. It is stored in a separate column of `migration_history` table. It could be used for downgrade operations | String  | -          | migration-context                              |
| schema.wizard.database.connection-url     | Database connection URL                                                                                                                               | String  | -          | jdbc:postgresql://localhost:5432/schema-wizard |
| schema.wizard.database.username           | Database username                                                                                                                                     | String  | -          | postgres                                       |
| schema.wizard.database.password           | Database password                                                                                                                                     | String  | -          | postgres                                       |
| schema.wizard.naming-strategy.column.type | Defines which strategy to use to generate names for table columns. Possible values: SNAKE_CASE, CAMEL_CASE                                            | enum    | SNAKE_CASE | CAMEL_CASE                                     |
| schema.wizard.migration.package-name      | Package to scan migrations from                                                                                                                       | String  | -          | com.example.schemawizard                       |
| schema.wizard.extension.package-name      | Package to scan extensions (callbacks, operation resolvers etc) from                                                                                  | String  | -          | com.example.extension                          |
| schema.wizard.log.sql-query               | Whether to log generated SQL queries                                                                                                                  | boolean | false      | true                                           |
| schema.wizard.defaults.text.max-length    | Default max length value for text column                                                                                                              | Integer | -          | 255                                            |

Example of the configuration file:

```yaml
schema:
  wizard:
    context: migration-context
    database:
      connection-url: jdbc:postgresql://localhost:5432/schema-wizard
      username: ${DB_USERNAME:postgres}
      password: ${DB_PASSWORD:postgres}
    naming-strategy:
      column:
        type: SNAKE_CASE
    migration:
      package-name: com.example.migration
    extension:
      package-name: com.example.extension
    log:
      sql-query: false
    defaults:
      text:
        max-length: 255
```

## Upgrade a database

### Creating a table

Consider a following SQL query which creates a table:

```sql
CREATE TABLE public.users
(
    id         INTEGER NOT NULL,
    email      TEXT    NOT NULL,
    first_name TEXT,
    last_name  TEXT,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT unq_users_email UNIQUE (email)
);
```

In order to run this script via SchemaWizard, you have to do:

1. Create a Java class in a package defined in `schema.wizard.migration.package-name` property of the configuration
   file. Also, the class has to have a name which satisfies the following pattern: SW<version>Description.
2. Implement `Migration` interface.
3. Implement `up()` and `down()` methods.
4. Run SchemaWizard library

#### Step 1 - Create a script in a package defined in `schema.wizard.migration.package-name` property of the configuration file.

```Java
package com.example.schemawizard;

public class SW001CreateUsersTable {

}
```

Here `001` is a version and `CreateUsersTable` is a Description. Also, you can define migration's description
with `@SWName` annotation - you will see an example in a following code fragment.

#### Step 2 - Implement `Migration` interface

```Java
package com.example.schemawizard;

import io.github.andriybosik.schemawizard.core.migration.annotation.SWName;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationContext;
import io.github.andriybosik.schemawizard.core.migration.operation.Operation;
import io.github.andriybosik.schemawizard.core.migration.Migration;

@SWName("Create users table")
public class SW001CreateUsersTable implements Migration {
    @Override
    public Operation up(MigrationContext context) {
        return null;
    }

    @Override
    public Operation down(MigrationContext context) {
        return null;
    }
}
```

#### Step 3 - Implement `up()` and `down()` methods.

```Java
package com.example.schemawizard;

import io.github.andriybosik.schemawizard.core.migration.annotation.SWName;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationContext;
import io.github.andriybosik.schemawizard.core.migration.operation.Operation;
import io.github.andriybosik.schemawizard.core.migration.Migration;

@SWName("Create users table")
public class SW001CreateUsersTable implements Migration {
    @Override
    public Operation up(MigrationContext context) {
        return CreateTable.builder(
                        "public", // schema name
                        "users", // table name
                        factory -> new Object() {
                            ColumnBuilder id = factory
                                        .newInteger() // column type - integer, column name - id
                                        .nullable(false); // not-nullable

                            ColumnBuilder email = factory
                                        .newText() // column type - text, column name - email
                                        .nullable(false); // not-nullable

                            ColumnBuilder firstName = factory
                                        .newText("first_name"); // column type - text, column name - first_name

                            ColumnBuilder lastName = factory
                                        .text("last_name"); // column type - text, column name - last_name
                        })
                .primaryKey("pk_users", table -> table.id) // CONSTRAINT pk_users PRIMARY KEY (id)
                .unique("unq_users_email", table -> table.email) // CONSTRAINT unq_users_email UNIQUE (email)
                .build();
    }

    @Override
    public Operation down(MigrationContext context) {
        return DropTable.builder("public", "users").build(); // DROP TABLE public.users;
    }
}
```

SchemaWizard uses `up()` method to run upgrade migrations and uses `down()` method to run downgrade migrations.

#### Step 4 - Run SchemaWizard library

In order to run SchemaWizard you have to create an instance of SchemaWizard using SchemaWizardBuilder:

```Java
SchemaWizard schemaWizard=SchemaWizardBuilder.init()
        .build();
```

And run an upgrade:

```Java
schemaWizard.up();
```

On its first run, the library will create `migration_history` table where all the migrations history is stored.
Every upgrade uses this table to compare current database state with migrations which are defined in your project.
Not-applied migrations will be applied and stored in the `migration_history` table.

### Creating a table with custom column and foreign key

Consider the following SQL query:

```sql
CREATE TABLE public.posts
(
    id          INTEGER NOT NULL,
    user_id     INTEGER NOT NULL,
    title       TEXT    NOT NULL,
    description TEXT    NOT NULL,
    details     JSON    NOT NULL DEFAULT JSON_BUILD_OBJECT(),
    CONSTRAINT pk_posts PRIMARY KEY (id),
    CONSTRAINT fk_posts_user_id FOREIGN KEY (user_id) REFERENCES public.users (id)
);
```

The corresponding Java class for this SQL script will look like:

```Java
public class SW002CreatePostsTable implements Migration {
    @Override
    public Operation up(MigrationContext context) {
        return CreateTable.builder(
                        "public",
                        "posts",
                        factory -> new Object() {
                            ColumnBuilder id = factory.newInteger().nullable(false);

                            ColumnBuilder userId = factory.newInteger().nullable(false); // if schema.wizard.naming-strategy.column.type was set to SNAKE_CASE, SchemaWizard will generate `user_id` name for this column

                            ColumnBuilder title = factory.newText().nullable(false);

                            ColumnBuilder description = factory.newText().nullable(false);

                            ColumnBuilder details = factory.generic().type("JSON").nullable(false).sqlDefault("JSON_BUILD_OBJECT()");
                        })
                .primaryKey("pk_posts", table -> table.id)
                .foreignKey("fk_posts_user_id", fk -> fk.column(table -> table.userId)
                        .foreignSchema("public")
                        .foreignTable("users")
                        .foreignColumn("id")) // CONSTRAINT fk_posts_user_id FOREIGN KEY (user_id) REFERENCES public.users (id)
                .build();
    }

    @Override
    public Operation down(MigrationContext context) {
        return DropTable.builder("public", "posts").build(); // DROP TABLE public.users;
    }
}
```

### Running a native SQL scripts

If you have native migrations which could not be translated into Java code, you also have a possibility to define an
SQL-native migration.
Let's create a file `db/migration/initial-up.sql` under `resources` directory:

```sql
ALTER TABLE SCHEMAWIZARD.posts
    ADD COLUMN IF NOT EXISTS column0 TEXT NOT NULL;
```

And use it in a new Java-based migration:

```Java
public class SW003NativeExample implements Migration {
    @Override
    public Operation up(MigrationContext context) {
        return NativeQuery.builder()
                .file("db/migration/initial-up.sql") // A path to a migration in a resources folder
                .sql("ALTER TABLE public.posts ADD column1 TEXT NOT NULL") // using raw SQL
                .build();
    }

    @Override
    public Operation down(MigrationContext context) {
        return DropColumns.builder("public", "posts")
                .columns("column0", "column1")
                .build();
    }
}
```
## Downgrade a database

SchemaWizard provides 3 ways to downgrade your database version:

1. Downgrade by count
2. Downgrade by version
3. Downgrade by context

### Downgrade by count

To trigger downgrading by count, the following code should be used:

```java
schemaWizard.downByCount(2);
```

In this case, SchemaWizard will search for 2 recently (recency is defined by applied date and not by version number) executed migration classes and will execute `down()` method.

### Downgrade by version

To trigger downgrading by version, the following code should be used:

```java
schemaWizard.downByVersion(1745424669072);
```

In this case, SchemaWizard will execute `down()` method for all the migrations that were executed after a migration with version number equal to 2.
Downgrade for migration with the provided version will be executed as well.
If a migration with a provided version number does not exist in the `migration_history` table - no migrations will be downgraded.

### Downgrade by context

To trigger downgrading by context, the following code should be used:

```java
schemaWizard.downByContext("migration-context");
```

In this case, SchemaWizard will execute downgrade for the migrations that have provided context in `context` column of `migration_history` table.
For example, let's consider the following `migration_history` table:

| id | version       | description                      | context           | applied_on                 |
|----|---------------|----------------------------------|-------------------|----------------------------|
| 1  | 1644935164376 | Create users table               | \<null\>          | 2022-02-15 20:08:02.297015 |
| 2  | 1644942227413 | Create posts table               | \<null\>          | 2022-02-15 20:08:02.368423 |
| 3  | 1645132160010 | Add idx_users_email index        | migration-context | 2022-02-17 23:57:41.282187 |
| 4  | 1645136472162 | Add status column to users table | migration-context | 2022-02-18 00:22:06.842839 |

When running the above command for this database, SchemaWizard will downgrade the migrations with `id` = 4 and 3.

**Note!** There might a case when there are multiple migrations with the same `context` value, but there are some migrations with a different `context` value between them. Like in the following example:

| id | version       | description                      | context        | applied_on                 |
|----|---------------|----------------------------------|----------------|----------------------------|
| 1  | 1644935164376 | Create users table               | feature/first  | 2022-02-15 20:08:02.297015 |
| 2  | 1644942227413 | Create posts table               | feature/second | 2022-02-15 20:08:02.368423 |
| 3  | 1645132160010 | Add idx_users_email index        | feature/first  | 2022-02-17 23:57:41.282187 |
| 4  | 1645136472162 | Add status column to users table | feature/first  | 2022-02-18 00:22:06.842839 |

In this case, when running the following command:

```java
schemaWizard.downByContext("feature/first");
```

Only the migrations with `id` = 4 and 3 will be downgraded.

## Other supported actions

SchemaWizard also supports the following actions:

1. `AddCheck` - add check constraint to a database table
2. `AddColumns` - add columns to a database table
3. `AddForeignKey` - add foreign key to a database table
4. `AddPrimaryKey` - add primary key to a database table
5. `AddUnique` - add unique constraint to a database table
6. `Composite` - compose multiple actions into one
7. `CreateIndex` - create an index
8. `CreateSequence` - create a sequence
9. `CreateTable` - create a database table
10. `DropCheck` - drop check constraint from a database table
11. `DropColumns` - drop columns from a database table
12. `DropConstraint` - drop constraint from a database table
13. `DropForeignKey` - drop foreign key from a database table
14. `DropIndex` - drop index
15. `DropPrimaryKey` - drop primary key from a database table
16. `DropTable` - drop a table
17. `DropUnique` - drop unique constraint from a database table
18. `NativeQuery` - run native queries

## Defining a custom Operation

You might notice that `up()` and `down()` both returns an instance of `Operation` interface.
`Operation` interface defines an action on a database.
Let's use this interface to define our custom action on a database.

Let's imagine that you use PostgreSQL and you would like to create a `ENUM` type, and you would like to not use SQL.
Instead, it would be better to use builder-approach as you did before.
To achieve this, you have to follow the next steps:

1. Create a class that implements `Operation` interface
2. Create a class that implements `OperationResolver<T extends Operation>` class
3. Use your operation in a migration class

### Step 1 - Create a class that implements `Operation` interface

```Java
public class CreateEnumOperation implements Operation {
    private final String schema;
    private final String enumName;
    private final String[] values;

    public CreateEnumOperation(
            String schema,
            String enumName,
            String... values
    ) {
        this.schema = schema;
        this.enumName = enumName;
        this.values = values;
    }

    public String getSchema() {
        return schema;
    }

    public String getEnumName() {
        return enumName;
    }

    public String[] getValues() {
        return values;
    }
}
```

`CreateEnumOperation` contains an information about the name of database schema, the name of enum you want to create,
and a list of values for the enum.

Let's also create a corresponding builder for this class:

```Java
public class CreateEnum {
    private String schema;
    private String enumName;
    private String[] values;

    private CreateEnum() {
    }

    public static CreateEnum builder() {
        return new CreateEnum();
    }

    public CreateEnum schema(String schema) {
        this.schema = schema;
        return this;
    }

    public CreateEnum enumName(String enumName) {
        this.enumName = enumName;
        return this;
    }

    public CreateEnum values(String... values) {
        this.values = values;
        return this;
    }

    public Operation build() {
        return new CreateEnumOperation(schema, enumName, values);
    }
}
```

### Step 2 - Create a class that implements `OperationResolver<T extends Operation>` class

**Note!** The class should be created under a package defined in `schema.wizard.extension.package-name` property.

`OperationResolver` is a class that is used to translate `Operation`'s metadata into native SQL. Let's take a look at
this interface:

```Java
public interface OperationResolver<T extends Operation> {
    MigrationInfo resolve(T operation);
}
```

```Java
public class MigrationInfo {
    private final String sql;

    public MigrationInfo() {
        this.sql = null;
    }

    public MigrationInfo(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }
}
```

So `OperationResolver` is an interface with one single method to implement - `MigrationInfo resolver(T operation)`
where `T` is a class which implements `Operation` interface.
`MigrationInfo` is a POJO class with only one field - `sql`. This field contains a final SQL script which will be run on
a database.

Let's take a look at the implementation of this interface for newly defined `CreateEnumOperation`:

```Java
public class CreateEnumOperationResolver implements OperationResolver<CreateEnumOperation> {
    @Override
    public MigrationInfo resolve(CreateEnumOperation operation) {
        return new MigrationInfo(
                String.format(
                        "CREATE TYPE %s.%s AS ENUM (%s)",
                        operation.getSchema(),
                        operation.getEnumName(),
                        Arrays.stream(operation.getValues())
                                .collect(Collectors.joining("', '", "'", "'"))
                )
        );
    }
}
```

The implementation just uses values, provided by `CreateEnumOperation` class to combine them into native script.

### Step 3 - Use your operation in a migration class

Finally, you can create a migration script that uses your custom defined `CreateEnumOperation`:

```Java
public class SW004CreateStatusEnum implements Migration {
    @Override
    public Operation up(MigrationContext context) {
        return CreateEnum.builder()
                .schema("public")
                .enumName("status")
                .values("ENABLED", "DISABLED", "PENDING")
                .build();
    }
}
```

Using `CreateEnumOperationResolver`, this script will be translated into

```sql
CREATE TYPE public.stats AS ENUM ('ENABLED', 'DISABLED', 'PENDING')
```

And will be run on your database.

**Note!** If you use multiple database providers(for example, PostgreSQL and Oracle), the
same `CreateEnumOperationResolver` will be used for both database.
Since Oracle does not a syntax for creating an enum, your migration will fail.
To avoid such issues, you have to implement two resolvers and annotate them with `@Provider` annotation:

```Java
import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;
import io.github.andriybosik.schemawizard.core.migration.annotation.Provider;

@Provider(DatabaseProvider.POSTGRESQL)
public class PostgreSqlCreateEnumOperationResolver implements OperationResolver<CreateEnumOperation> {
    @Override
    public MigrationInfo resolve(CreateEnumOperation operation) {
        // ...
    }
}
```

```Java
import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;
import io.github.andriybosik.schemawizard.core.migration.annotation.Provider;

@Provider(DatabaseProvider.ORACLE)
public class OracleCreateEnumOperationResolver implements OperationResolver<CreateEnumOperation> {
    @Override
    public MigrationInfo resolve(CreateEnumOperation operation) {
        // ...
    }
}
```

## Using callbacks

SchemaWizard provides an ability to execute custom logic before or after some action is happened.
Currently supported callbacks are:

1. `AfterQueryExecutionCallback` - executes each time after an SQL query was run on a database
2. `BeforeQueryExecutionCallback` - executes each time before an SQL query will be run on a database
3. `QueryGeneratedCallback` - executes each time after Java class was translated into native SQL

The procedure to implement a callback is the similar to defining a custom `Operation`. You have to:

1. Create a class that implements a corresponding callback interface
2. Run your migrations

### Step 1 - Create a class that implements a corresponding callback interface

**Note!** The class should be created under a package defined in `schema.wizard.extension.package-name` property.

Let's define a callback which executes each time before an SQL query will be run on a database:

```Java
public class BeforeCallback implements BeforeQueryExecutionCallback {
    private final Logger log = LoggerFactory.getLogger(BeforeCallback.class);

    @Override
    public void handle(MigrationData data) {
        log.info("Running: {}; Description: {}", data.getVersion(), data.getDescription());
    }
}
```

### Step 2 - Run your migrations

If you run `schemaWizard.up()`, you will see the following logs:

```
[main] INFO com.example.schemawizard.BeforeCallback - Running: 1; Description: Create users table
[main] INFO com.example.schemawizard.BeforeCallback - Running: 2; Description: create posts table
```

## Plugins

SchemaWizard library provides Gradle and Maven plugins that could be used to execute library's functionality from command line.
It makes the library easily integrated with any CI/CD tools.

### Installation

To include Gradle plugin to your project, add the following plugin to your build.gradle:

```groovy
plugins {
    id "io.github.andriybosik.schema-wizard-gradle-plugin" version "${version}"
}
```

To include Maven plugin to your project, add the following plugin to your pom.xml:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>io.github.andriybosik</groupId>
            <artifactId>schema-wizard-maven-plugin-preview</artifactId>
            <version>${version}</version>
            <executions>
                <execution>
                    <goals>
                        <goal>upgrade</goal>
                        <goal>downgrade</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

### Usage

1. Upgrade:
   1. Gradle: `./gradlew :upgrade`
   2. Maven:  `./mvnw sw:upgrade`
2. Downgrade by count:
   1. Gradle: `./gradlew :downgrade -Psw.count=2`
   2. Maven:  `./mvnw sw:downgrade -Dsw.count=2`
3. Downgrade by version:
   1. Gradle: `./gradlew :downgrade -Psw.version=1745424669072`
   2. Maven:  `./mvnw sw:downgrade -Dsw.version=1745424669072`
4. Downgrade by context:
   1. Gradle: `./gradlew :downgrade -Psw.context=migration-context`
   2. Maven:  `./mvnw sw:downgrade -Dsw.context=migration-context`
