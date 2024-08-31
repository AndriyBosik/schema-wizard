package org.schemawizard.core.test;

import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.extension.ExtendWith;
import org.schemawizard.core.config.impl.EnvironmentExtension;
import org.schemawizard.core.config.impl.ResetDbExtension;
import org.schemawizard.core.starter.SchemaWizard;
import org.schemawizard.core.utils.FactoryUtils;

@DBRider
@ExtendWith({
        EnvironmentExtension.class,
        ResetDbExtension.class})
public class BaseTest {
    static {
        SchemaWizard schemaWizard = FactoryUtils.newInstance("none");
        schemaWizard.up();
    }
}
