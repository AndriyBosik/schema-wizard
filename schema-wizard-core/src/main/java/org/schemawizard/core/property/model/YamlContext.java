package org.schemawizard.core.property.model;


public class YamlContext {
    private Schema schema = new Schema();

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public static class Schema {
        private Wizard wizard = new Wizard();

        public Wizard getWizard() {
            return wizard;
        }

        public void setWizard(Wizard wizard) {
            this.wizard = wizard;
        }

        public static class Wizard {
            private String context;
            private Database database = new Database();
            private Migration migration = new Migration();
            private Extension extension = new Extension();
            private Log log = new Log();
            private Defaults defaults = new Defaults();
            private NamingStrategy namingStrategy = new NamingStrategy();

            public Defaults getDefaults() {
                return defaults;
            }

            public void setDefaults(Defaults defaults) {
                this.defaults = defaults;
            }

            public String getContext() {
                return context;
            }

            public void setContext(String context) {
                this.context = context;
            }

            public Database getDatabase() {
                return database;
            }

            public void setDatabase(Database database) {
                this.database = database;
            }

            public Migration getMigration() {
                return migration;
            }

            public void setMigration(Migration migration) {
                this.migration = migration;
            }

            public Extension getExtension() {
                return extension;
            }

            public void setExtension(Extension extension) {
                this.extension = extension;
            }

            public Log getLog() {
                return log;
            }

            public void setLog(Log log) {
                this.log = log;
            }

            public NamingStrategy getNamingStrategy() {
                return namingStrategy;
            }

            public void setNamingStrategy(NamingStrategy namingStrategy) {
                this.namingStrategy = namingStrategy;
            }

            public static class Database {
                private String connectionUrl;
                private String username;
                private String password;

                public String getConnectionUrl() {
                    return connectionUrl;
                }

                public void setConnectionUrl(String connectionUrl) {
                    this.connectionUrl = connectionUrl;
                }

                public String getUsername() {
                    return username;
                }

                public void setUsername(String username) {
                    this.username = username;
                }

                public String getPassword() {
                    return password;
                }

                public void setPassword(String password) {
                    this.password = password;
                }
            }

            public static class Migration {
                private String packageName;

                public String getPackageName() {
                    return packageName;
                }

                public void setPackageName(String packageName) {
                    this.packageName = packageName;
                }
            }

            public static class Extension {
                private String packageName;

                public String getPackageName() {
                    return packageName;
                }

                public void setPackageName(String packageName) {
                    this.packageName = packageName;
                }
            }

            public static class Log {
                private String sqlQuery;

                public String getSqlQuery() {
                    return sqlQuery;
                }

                public void setSqlQuery(String sqlQuery) {
                    this.sqlQuery = sqlQuery;
                }
            }

            public static class Defaults {

                private Text text = new Text();

                public Text getText() {
                    return text;
                }

                public void setText(Text text) {
                    this.text = text;
                }

                public static class Text {
                    private String maxLength;

                    public String getMaxLength() {
                        return maxLength;
                    }

                    public void setMaxLength(String maxLength) {
                        this.maxLength = maxLength;
                    }
                }
            }

            public static class NamingStrategy {
                private Column column;

                public Column getColumn() {
                    return column;
                }

                public void setColumn(Column column) {
                    this.column = column;
                }

                public static class Column {
                    private String type;

                    public String getType() {
                        return type;
                    }

                    public void setType(String type) {
                        this.type = type;
                    }
                }
            }
        }
    }
}
