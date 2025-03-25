package org.schemawizard.core.config.impl;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.schemawizard.core.annotation.PostResetDb;
import org.schemawizard.core.annotation.PreResetDb;
import org.schemawizard.core.utils.EnvUtils;
import org.schemawizard.core.utils.TestIOUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ResetDbExtension implements BeforeEachCallback, AfterEachCallback, AfterAllCallback {
    @Override
    public void beforeEach(ExtensionContext context) {
        context.getTestMethod()
                .filter(method -> method.isAnnotationPresent(PreResetDb.class))
                .map(method -> method.getAnnotation(PreResetDb.class))
                .ifPresent(annotation -> resetDb(annotation.drop(), annotation.clean(), annotation.execute()));
    }

    @Override
    public void afterEach(ExtensionContext context) {
        context.getTestMethod()
                .filter(method -> method.isAnnotationPresent(PostResetDb.class))
                .map(method -> method.getAnnotation(PostResetDb.class))
                .ifPresent(annotation -> resetDb(annotation.drop(), annotation.clean(), annotation.execute()));
    }

    @Override
    public void afterAll(ExtensionContext context) {
        ResetQueryTemplate queryTemplate = getQueryTemplate();
        applyQuery(queryTemplate.getDrop(), new String[]{"migration_history", "people", "posts"});
    }

    private void resetDb(String[] dropTables, String[] cleanTables, String[] execute) {
        ResetQueryTemplate queryTemplate = getQueryTemplate();

        if (cleanTables.length > 0) {
            applyQuery(queryTemplate.getClean(), cleanTables);
        }
        if (dropTables.length > 0) {
            applyQuery(queryTemplate.getDrop(), dropTables);
        }

        if (execute.length > 0) {
            List<String> scripts = Arrays.stream(execute)
                    .map(folder -> TestIOUtils.getResourceFile(
                            String.format(
                                    "script/%s/%s.sql",
                                    folder,
                                    getFileName())))
                    .map(TestIOUtils::getContent)
                    .collect(Collectors.toList());

            execute(scripts);
        }
    }

    private void applyQuery(Function<String, String> query, String[] tables) {
        try (
                Connection connection = DriverManager.getConnection(EnvUtils.DB_URL, EnvUtils.DB_USERNAME, EnvUtils.DB_PASSWORD);
                Statement statement = connection.createStatement()
        ) {
            for (String table : tables) {
                String sql = query.apply(table);
                statement.execute(sql);
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void execute(List<String> scripts) {
        try (
                Connection connection = DriverManager.getConnection(EnvUtils.DB_URL, EnvUtils.DB_USERNAME, EnvUtils.DB_PASSWORD);
                Statement statement = connection.createStatement()
        ) {
            for (String script : scripts) {
                statement.execute(script);
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    private ResetQueryTemplate getQueryTemplate() {
        switch (EnvUtils.PROVIDER) {
            case POSTGRESQL:
            case ORACLE:
                return new ResetQueryTemplate(
                        tableName -> "DELETE FROM " + tableName,
                        tableName -> "DROP TABLE IF EXISTS " + tableName);
            default:
                throw new IllegalArgumentException("Unknown directory for database provider: " + EnvUtils.PROVIDER);
        }
    }

    private String getFileName() {
        switch (EnvUtils.PROVIDER) {
            case POSTGRESQL:
                return "postgresql";
            case ORACLE:
                return "oracle";
        }
        throw new IllegalArgumentException("Unknown database provider: " + EnvUtils.PROVIDER);
    }

    private static class ResetQueryTemplate {
        private final Function<String, String> clean;
        private final Function<String, String> drop;

        public ResetQueryTemplate(Function<String, String> clean, Function<String, String> drop) {
            this.clean = clean;
            this.drop = drop;
        }

        public Function<String, String> getClean() {
            return clean;
        }

        public Function<String, String> getDrop() {
            return drop;
        }
    }
}
