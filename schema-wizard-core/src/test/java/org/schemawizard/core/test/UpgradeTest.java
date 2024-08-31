package org.schemawizard.core.test;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import org.junit.jupiter.api.Test;
import org.schemawizard.core.annotation.PreResetDb;
import org.schemawizard.core.starter.SchemaWizard;
import org.schemawizard.core.utils.FactoryUtils;

public class UpgradeTest extends BaseTest {
    @Test
    @PreResetDb(drop = {"users", "posts"})
    @ExpectedDataSet("/dataset/upgrade/empty-database/expected.json")
    void shouldUpgradeWithEmptyDatabase() {
        SchemaWizard schemaWizard = FactoryUtils.newInstance("emptyhistory");
        schemaWizard.up();
    }

    @Test
    @PreResetDb(drop = {"users", "posts"})
    @DataSet("/dataset/upgrade/subsequent/initial.json")
    @ExpectedDataSet(value = "/dataset/upgrade/subsequent/expected.json", orderBy = "version")
    void shouldUpgradeSubsequentWithContext() {
        SchemaWizard schemaWizard = FactoryUtils.newInstance("subsequent", "subsequent-upgrade");
        schemaWizard.up();
    }
}
