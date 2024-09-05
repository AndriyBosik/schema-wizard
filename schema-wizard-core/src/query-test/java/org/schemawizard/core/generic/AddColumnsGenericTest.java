package org.schemawizard.core.generic;

import org.junit.jupiter.api.Test;
import org.schemawizard.core.config.GenericTest;
import org.schemawizard.core.migration.builder.operation.AddColumns;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.Operation;

import java.util.List;

public class AddColumnsGenericTest extends GenericTest {
    @Test
    public void shouldGenerateBooleanColumnNotNull() {
        Operation operation = AddColumns.builder("users", factory -> List.of(factory.newBool("enabled").nullable(false)))
                .build();
        MigrationInfo info = operationResolverService.resolve(operation);

        assertQuery("add-enabled-column", info.getSql());
    }
}
