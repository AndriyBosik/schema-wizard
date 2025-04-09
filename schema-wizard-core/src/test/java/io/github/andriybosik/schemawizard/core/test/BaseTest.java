package io.github.andriybosik.schemawizard.core.test;

import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.extension.ExtendWith;
import io.github.andriybosik.schemawizard.core.config.impl.EnvironmentExtension;
import io.github.andriybosik.schemawizard.core.config.impl.MigrationHistoryExtension;
import io.github.andriybosik.schemawizard.core.config.impl.ResetDbExtension;

@DBRider
@ExtendWith({
        EnvironmentExtension.class,
        MigrationHistoryExtension.class,
        ResetDbExtension.class})
public abstract class BaseTest {
}
