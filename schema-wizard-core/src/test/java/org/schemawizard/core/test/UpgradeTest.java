package org.schemawizard.core.test;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import org.junit.jupiter.api.Test;
import org.schemawizard.core.annotation.PreResetDb;
import org.schemawizard.core.extension.upgrade.callbacks.AfterCallback;
import org.schemawizard.core.extension.upgrade.callbacks.BeforeCallback;
import org.schemawizard.core.starter.SchemaWizard;
import org.schemawizard.core.utils.FactoryUtils;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpgradeTest extends BaseTest {
    @Test
    @PreResetDb(drop = {"people", "posts"}, clean = "migration_history")
    @ExpectedDataSet("/dataset/upgrade/empty-database/expected.json")
    void shouldUpgradeWithEmptyDatabase() {
        SchemaWizard schemaWizard = FactoryUtils.newInstance("upgrade.emptyhistory");
        schemaWizard.up();
    }

    @Test
    @PreResetDb(drop = {"people", "posts"}, clean = "migration_history")
    @DataSet("/dataset/upgrade/subsequent/initial.json")
    @ExpectedDataSet(value = "/dataset/upgrade/subsequent/expected.json", orderBy = "version")
    void shouldUpgradeSubsequentWithContext() {
        SchemaWizard schemaWizard = FactoryUtils.newInstance("upgrade.subsequent", "subsequent-upgrade");
        schemaWizard.up();
    }

    @Test
    @PreResetDb(drop = {"people", "posts"}, clean = "migration_history")
    @DataSet("/dataset/upgrade/unordered/initial.json")
    @ExpectedDataSet(value = "/dataset/upgrade/unordered/expected.json", orderBy = "version")
    void shouldUpgradeUnordered() {
        SchemaWizard schemaWizard = FactoryUtils.newInstance("upgrade.unordered");
        schemaWizard.up();
    }

    @Test
    @PreResetDb(drop = {"people", "posts"}, clean = "migration_history")
    @DataSet("/dataset/common/empty/initial.json")
    @ExpectedDataSet("/dataset/upgrade/callbacks/expected.json")
    void shouldExecuteCallbacks() {
        SchemaWizard schemaWizard = FactoryUtils.newInstance("upgrade.callbacks");
        schemaWizard.up();

        assertEquals(2, BeforeCallback.getExecutions().size());
        assertEquals(2, AfterCallback.getExecutions().size());

        assertTrue(compare(BeforeCallback.getExecutions().get(0), AfterCallback.getExecutions().get(0)) < 0);
        assertTrue(compare(AfterCallback.getExecutions().get(0), BeforeCallback.getExecutions().get(1)) <= 0);
        assertTrue(compare(BeforeCallback.getExecutions().get(1), AfterCallback.getExecutions().get(1)) < 0);
    }

    private int compare(ZonedDateTime first, ZonedDateTime second) {
        return first.compareTo(second);
    }
}
