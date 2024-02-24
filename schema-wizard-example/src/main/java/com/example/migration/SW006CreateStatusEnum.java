package com.example.migration;

import com.example.extension.resolver.operation.CreateEnum;
import com.example.extension.resolver.operation.DropEnum;
import org.schemawizard.core.migration.Migration;
import org.schemawizard.core.migration.model.MigrationContext;
import org.schemawizard.core.migration.operation.Operation;

public class SW006CreateStatusEnum implements Migration {
    @Override
    public Operation up(MigrationContext context) {
        return CreateEnum.builder()
                .schema("public")
                .enumName("status")
                .values("ENABLED", "DISABLED", "PENDING")
                .build();
    }

    @Override
    public Operation down(MigrationContext context) {
        return DropEnum.builder()
                .schema("public")
                .enumName("status")
                .build();
    }
}
