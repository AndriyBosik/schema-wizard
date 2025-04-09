package io.github.andriybosik.schemawizard.core.test;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import org.junit.jupiter.api.Test;
import io.github.andriybosik.schemawizard.core.analyzer.exception.MigrationAnalyzerException;
import io.github.andriybosik.schemawizard.core.annotation.PostResetDb;
import io.github.andriybosik.schemawizard.core.annotation.PreResetDb;
import io.github.andriybosik.schemawizard.core.starter.SchemaWizard;
import io.github.andriybosik.schemawizard.core.utils.FactoryUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DowngradeTest extends BaseTest {
    @Test
    @PreResetDb(drop = {"people", "posts"}, clean = "migration_history", execute = {"people-with-age", "posts"})
    @DataSet("/dataset/downgrade/count/initial/initial.json")
    @ExpectedDataSet(value = "/dataset/downgrade/count/initial/expected.json", orderBy = "version")
    void shouldDowngradeMigrationsByCount() {
        SchemaWizard schemaWizard = FactoryUtils.newInstance("downgrade.count.initial");
        schemaWizard.downByCount(2);
    }

    @Test
    @PreResetDb(drop = {"people", "posts"}, clean = "migration_history", execute = {"people-with-age", "posts"})
    @DataSet("/dataset/downgrade/count/zero/initial.json")
    @ExpectedDataSet(value = "/dataset/downgrade/count/zero/expected.json", orderBy = "version")
    void shouldNotDowngradeWhenCountIsZero() {
        SchemaWizard schemaWizard = FactoryUtils.newInstance("downgrade.count.zero");
        schemaWizard.downByCount(0);
    }

    @Test
    @PreResetDb(drop = {"people", "posts"}, clean = "migration_history", execute = {"people-with-age", "posts"})
    @DataSet("/dataset/downgrade/count/negative/initial.json")
    @ExpectedDataSet(value = "/dataset/downgrade/count/negative/expected.json", orderBy = "version")
    void shouldThrowExceptionWhenCountIsNegative() {
        SchemaWizard schemaWizard = FactoryUtils.newInstance("downgrade.count.negative");
        MigrationAnalyzerException exception = assertThrows(MigrationAnalyzerException.class, () -> schemaWizard.downByCount(-10));
        assertEquals("Downgrade count of migrations cannot be negative: -10", exception.getMessage());
    }

    @Test
    @PreResetDb(drop = {"people", "posts"}, clean = "migration_history", execute = {"people-with-age", "posts"})
    @DataSet("/dataset/downgrade/count/greater/initial.json")
    @ExpectedDataSet(value = "/dataset/downgrade/count/greater/expected.json", orderBy = "version")
    void shouldThrowExceptionWhenCountIsGreaterThanAppliedMigrationsCount() {
        SchemaWizard schemaWizard = FactoryUtils.newInstance("downgrade.count.greater");
        MigrationAnalyzerException exception = assertThrows(MigrationAnalyzerException.class, () -> schemaWizard.downByCount(10));
        assertEquals("Unable to downgrade migrations by count. 3 applied migrations were found, but 10 were requested to be downgraded", exception.getMessage());
    }

    @Test
    @PreResetDb(drop = {"people", "posts"}, clean = "migration_history", execute = {"rename-migration-history-to-history"})
    @PostResetDb(execute = {"rename-history-to-migration-history"})
    void shouldThrowExceptionWhenDowngradeByCountAndMigrationHistoryTableDoesNotExist() {
        SchemaWizard schemaWizard = FactoryUtils.newInstance("none");
        MigrationAnalyzerException exception = assertThrows(MigrationAnalyzerException.class, () -> schemaWizard.downByCount(1));
        assertEquals("No migrations has been found, run upgrade first", exception.getMessage());
    }

    @Test
    @PreResetDb(drop = {"people", "posts"}, clean = "migration_history", execute = {"people-with-age", "posts-with-description"})
    @DataSet("/dataset/downgrade/context/initial/initial.json")
    @ExpectedDataSet(value = "/dataset/downgrade/context/initial/expected.json", orderBy = "version")
    void shouldDowngradeByStrippedContext() {
        SchemaWizard schemaWizard = FactoryUtils.newInstance("downgrade.context.initial");
        schemaWizard.downByContext("    test-context ");
    }

    @Test
    @PreResetDb(drop = {"people", "posts"}, clean = "migration_history", execute = {"people-with-age", "posts-with-description"})
    @DataSet("/dataset/downgrade/context/last-null/initial.json")
    @ExpectedDataSet(value = "/dataset/downgrade/context/last-null/expected.json", orderBy = "version")
    void shouldNotDowngradeWhenLastContextValueIsNull() {
        SchemaWizard schemaWizard = FactoryUtils.newInstance("downgrade.context.lastnull");
        schemaWizard.downByContext("test-context");
    }

    @Test
    @PreResetDb(drop = {"people", "posts"}, clean = "migration_history", execute = {"people-with-age", "posts-with-description"})
    @DataSet("/dataset/downgrade/context/not-match/initial.json")
    @ExpectedDataSet(value = "/dataset/downgrade/context/not-match/expected.json", orderBy = "version")
    void shouldNotDowngradeWhenLastContextValueDoesNotMatch() {
        SchemaWizard schemaWizard = FactoryUtils.newInstance("downgrade.context.notmatch");
        schemaWizard.downByContext("test-context");
    }

    @Test
    @PreResetDb(drop = {"people", "posts"}, clean = "migration_history", execute = {"people-with-age", "posts-with-description"})
    @DataSet("/dataset/downgrade/context/all/initial.json")
    @ExpectedDataSet(value = "/dataset/downgrade/context/all/expected.json", orderBy = "version")
    void shouldDowngradeAllByContext() {
        SchemaWizard schemaWizard = FactoryUtils.newInstance("downgrade.context.all");
        schemaWizard.downByContext("test-context");
    }

    @Test
    @PreResetDb(drop = {"people", "posts"}, clean = "migration_history", execute = {"people-with-age", "posts-with-description"})
    @DataSet("/dataset/downgrade/context/nullable/initial.json")
    @ExpectedDataSet(value = "/dataset/downgrade/context/nullable/expected.json", orderBy = "version")
    void shouldThrowExceptionWhenContextIsNull() {
        SchemaWizard schemaWizard = FactoryUtils.newInstance("downgrade.context.nullable");
        MigrationAnalyzerException exception = assertThrows(MigrationAnalyzerException.class, () -> schemaWizard.downByContext(null));
        assertEquals("Context value 'null' is blank", exception.getMessage());
    }

    @Test
    @PreResetDb(drop = {"people", "posts"}, clean = "migration_history", execute = {"people-with-age", "posts-with-description"})
    @DataSet("/dataset/downgrade/context/empty/initial.json")
    @ExpectedDataSet(value = "/dataset/downgrade/context/empty/expected.json", orderBy = "version")
    void shouldThrowExceptionWhenContextIsEmpty() {
        SchemaWizard schemaWizard = FactoryUtils.newInstance("downgrade.context/empty");
        MigrationAnalyzerException exception = assertThrows(MigrationAnalyzerException.class, () -> schemaWizard.downByContext("     \n\n\t\t   "));
        assertEquals("Context value '     \n\n\t\t   ' is blank", exception.getMessage());
    }

    @Test
    @PreResetDb(drop = {"people", "posts"}, clean = "migration_history", execute = {"rename-migration-history-to-history"})
    @PostResetDb(execute = {"rename-history-to-migration-history"})
    void shouldThrowExceptionWhenDowngradeByContextAndMigrationHistoryTableDoesNotExist() {
        SchemaWizard schemaWizard = FactoryUtils.newInstance("none");
        MigrationAnalyzerException exception = assertThrows(MigrationAnalyzerException.class, () -> schemaWizard.downByContext("test-context"));
        assertEquals("No migrations has been found, run upgrade first", exception.getMessage());
    }

    @Test
    @PreResetDb(drop = {"people", "posts"}, clean = "migration_history", execute = {"people-with-age", "posts-with-description"})
    @DataSet("/dataset/downgrade/version/initial/initial.json")
    @ExpectedDataSet(value = "/dataset/downgrade/version/initial/expected.json", orderBy = "version")
    void shouldDowngradeByVersion() {
        SchemaWizard schemaWizard = FactoryUtils.newInstance("downgrade.version.initial");
        schemaWizard.downByVersion(124);
    }

    @Test
    @PreResetDb(drop = {"people", "posts"}, clean = "migration_history", execute = {"people-with-age", "posts-with-description"})
    @DataSet("/dataset/downgrade/version/not-ordered/initial.json")
    @ExpectedDataSet(value = "/dataset/downgrade/version/not-ordered/expected.json", orderBy = "version")
    void shouldDowngradeByNotOrderedVersions() {
        SchemaWizard schemaWizard = FactoryUtils.newInstance("downgrade.version.notordered");
        schemaWizard.downByVersion(222);
    }

    @Test
    @PreResetDb(drop = {"people", "posts"}, clean = "migration_history", execute = {"people-with-age", "posts-with-description"})
    @DataSet("/dataset/downgrade/version/non-existent/initial.json")
    @ExpectedDataSet(value = "/dataset/downgrade/version/non-existent/expected.json", orderBy = "version")
    void shouldNotDowngradeByNonExistentVersion() {
        SchemaWizard schemaWizard = FactoryUtils.newInstance("downgrade.version.nonexistent");
        schemaWizard.downByVersion(-342);
    }

    @Test
    @PreResetDb(drop = {"people", "posts"}, clean = "migration_history", execute = {"people-with-age", "posts-with-description"})
    @DataSet("/dataset/downgrade/version/all/initial.json")
    @ExpectedDataSet(value = "/dataset/downgrade/version/all/expected.json", orderBy = "version")
    void shouldDowngradeAllByVersion() {
        SchemaWizard schemaWizard = FactoryUtils.newInstance("downgrade.version.all");
        schemaWizard.downByVersion(123);
    }

    @Test
    @PreResetDb(drop = {"people", "posts"}, clean = "migration_history", execute = {"rename-migration-history-to-history"})
    @PostResetDb(execute = {"rename-history-to-migration-history"})
    void shouldThrowExceptionWhenDowngradeByVersionAndMigrationHistoryTableDoesNotExist() {
        SchemaWizard schemaWizard = FactoryUtils.newInstance("none");
        MigrationAnalyzerException exception = assertThrows(MigrationAnalyzerException.class, () -> schemaWizard.downByVersion(123));
        assertEquals("No migrations has been found, run upgrade first", exception.getMessage());
    }
}
