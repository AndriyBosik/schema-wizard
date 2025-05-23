package io.github.andriybosik.schemawizard.core.test;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import org.junit.jupiter.api.Test;
import io.github.andriybosik.schemawizard.core.analyzer.exception.MigrationAnalyzerException;
import io.github.andriybosik.schemawizard.core.annotation.PreResetDb;
import io.github.andriybosik.schemawizard.core.starter.SchemaWizard;
import io.github.andriybosik.schemawizard.core.utils.FactoryUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MigrationValidationTest extends BaseTest {
    @Test
    @PreResetDb(drop = {"people", "posts"}, clean = "migration_history")
    @DataSet("/dataset/validation/fail-when-not-found/initial.json")
    @ExpectedDataSet(value = "/dataset/validation/fail-when-not-found/expected.json", orderBy = "version", ignoreCols = "id")
    void shouldThrowExceptionWhenMigrationNotFound() {
        SchemaWizard schemaWizard = FactoryUtils.newInstance("validation.failwhennotfound");
        MigrationAnalyzerException exception = assertThrows(MigrationAnalyzerException.class, schemaWizard::up);
        assertEquals("Migration with version 2 was not found", exception.getMessage());
    }

    @Test
    @PreResetDb(drop = {"people", "posts"}, clean = "migration_history")
    @DataSet("/dataset/validation/fail-when-duplicate-version/initial.json")
    @ExpectedDataSet(value = "/dataset/validation/fail-when-duplicate-version/expected.json", orderBy = "version", ignoreCols = "id")
    void shouldThrowExceptionWhenDuplicateVersion() {
        SchemaWizard schemaWizard = FactoryUtils.newInstance("validation.failwhenduplicateversion");
        MigrationAnalyzerException exception = assertThrows(MigrationAnalyzerException.class, schemaWizard::up);
        assertEquals("Multiple migrations with version 1 were found", exception.getMessage());
    }

    @Test
    @PreResetDb(drop = {"people", "posts"}, clean = "migration_history")
    @DataSet("/dataset/common/empty/initial.json")
    @ExpectedDataSet(value = "/dataset/common/empty/initial.json", ignoreCols = "id")
    void shouldThrowExceptionWhenIncorrectMigrationNamePattern() {
        SchemaWizard schemaWizard = FactoryUtils.newInstance("validation.incorrectmigrationnamepattern");
        MigrationAnalyzerException exception = assertThrows(MigrationAnalyzerException.class, schemaWizard::up);
        assertEquals("Class name 'SWDescriptionAndVersion001' doesn't match the pattern 'SW<version><description>'", exception.getMessage());
    }

    @Test
    @PreResetDb(drop = {"people", "posts"}, clean = "migration_history")
    @DataSet("/dataset/common/empty/initial.json")
    @ExpectedDataSet(value = "/dataset/common/empty/expected.json", ignoreCols = "id")
    void shouldThrowExceptionWhenMigrationConstructorHasParams() {
        SchemaWizard schemaWizard = FactoryUtils.newInstance("validation.constructionwithparams");
        MigrationAnalyzerException exception = assertThrows(MigrationAnalyzerException.class, schemaWizard::up);
        assertEquals("io.github.andriybosik.schemawizard.core.migration.validation.constructionwithparams.SW001CreatePeopleTable constructor shouldn't have params", exception.getMessage());
    }

    @Test
    @PreResetDb(drop = {"people", "posts"}, clean = "migration_history")
    @DataSet("/dataset/common/empty/initial.json")
    @ExpectedDataSet(value = "/dataset/common/empty/expected.json", ignoreCols = "id")
    void shouldThrowExceptionWhenPrivateMigrationConstructor() {
        SchemaWizard schemaWizard = FactoryUtils.newInstance("validation.privateconstrctor");
        MigrationAnalyzerException exception = assertThrows(MigrationAnalyzerException.class, schemaWizard::up);
        assertEquals("io.github.andriybosik.schemawizard.core.migration.validation.privateconstrctor.SW001CreatePeopleTable constructor should be 'public'", exception.getMessage());
    }

    @Test
    @PreResetDb(drop = {"people", "posts"}, clean = "migration_history")
    @DataSet("/dataset/common/empty/initial.json")
    @ExpectedDataSet(value = "/dataset/common/empty/expected.json", ignoreCols = "id")
    void shouldThrowExceptionWhenMultipleConstructorsDeclared() {
        SchemaWizard schemaWizard = FactoryUtils.newInstance("validation.multipleconstructors");
        MigrationAnalyzerException exception = assertThrows(MigrationAnalyzerException.class, schemaWizard::up);
        assertEquals("io.github.andriybosik.schemawizard.core.migration.validation.multipleconstructors.SW001CreatePeopleTable should have only one public constructor without params", exception.getMessage());
    }
}
