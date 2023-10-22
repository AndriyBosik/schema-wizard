package com.example;

import com.example.builder.model.MigrationContext;
import com.example.builder.operation.Operation;

public interface Migration {
    Operation up(MigrationContext context);

    Operation down(MigrationContext context);
}
