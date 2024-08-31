package org.schemawizard.core.config.impl;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.schemawizard.core.annotation.PreResetDb;
import org.schemawizard.core.utils.EnvUtils;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Function;

public class ResetDbExtension implements BeforeEachCallback, AfterAllCallback {
    @Override
    public void beforeEach(ExtensionContext context) {
        context.getTestMethod()
                .filter(method -> method.isAnnotationPresent(PreResetDb.class))
                .ifPresent(this::resetDb);
    }

    @Override
    public void afterAll(ExtensionContext context) {
        ResetQueryTemplate queryTemplate = getQueryTemplate();
        applyQuery(queryTemplate.getDrop(), new String[]{"migration_history", "people", "posts"});
    }

    private void resetDb(Method method) {
        PreResetDb annotation = method.getAnnotation(PreResetDb.class);
        ResetQueryTemplate queryTemplate = getQueryTemplate();

        String[] cleanTables = annotation.clean();
        if (cleanTables.length > 0) {
            applyQuery(queryTemplate.getClean(), cleanTables);
        }
        String[] dropTables = annotation.drop();
        if (dropTables.length > 0) {
            applyQuery(queryTemplate.getDrop(), dropTables);
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
