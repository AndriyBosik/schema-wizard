package org.schemawizard.core.migration.builder.column;

public abstract class AbstractColumnBuilder implements ColumnBuilder {
  protected final String schema;
  protected final String table;
  protected String name;

  protected AbstractColumnBuilder(String schema, String table, String name) {
    this.schema = schema;
    this.table = table;
    this.name = name;
  }
}
