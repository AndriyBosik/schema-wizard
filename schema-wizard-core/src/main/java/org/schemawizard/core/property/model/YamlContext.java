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
            private Database database = new Database();
            private Migration migration = new Migration();
            private Log log = new Log();

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

            public Log getLog() {
                return log;
            }

            public void setLog(Log log) {
                this.log = log;
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

            public static class Log {
                private String sqlQuery;

                public String getSqlQuery() {
                    return sqlQuery;
                }

                public void setSqlQuery(String sqlQuery) {
                    this.sqlQuery = sqlQuery;
                }
            }
        }
    }
}
