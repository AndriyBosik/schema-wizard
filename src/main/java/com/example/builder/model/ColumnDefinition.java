package com.example.builder.model;

public class ColumnDefinition {
    private String name;
    private boolean nullable;

    public ColumnDefinition() {}

    public ColumnDefinition(String name, boolean nullable) {
        this.name = name;
        this.nullable = nullable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }
}
