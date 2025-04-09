package io.github.andriybosik.schemawizard.core.utils;

import org.jetbrains.annotations.NotNull;
import io.github.andriybosik.schemawizard.core.property.model.YamlContext;
import io.github.andriybosik.schemawizard.core.starter.SchemaWizard;
import io.github.andriybosik.schemawizard.core.starter.SchemaWizardBuilder;

public class FactoryUtils {
    private FactoryUtils() {
    }

    public static SchemaWizard newInstance(String migrationType) {
        return newInstance(migrationType, null);
    }

    public static SchemaWizard newInstance(String migrationType, String context) {
        YamlContext.Schema.Wizard wizard = newWizard(migrationType, context);

        YamlContext.Schema schema = new YamlContext.Schema();
        schema.setWizard(wizard);

        YamlContext yamlContext = new YamlContext();
        yamlContext.setSchema(schema);

        return SchemaWizardBuilder.init(yamlContext)
                .build();
    }

    private static YamlContext.Schema.@NotNull Wizard newWizard(String migrationType, String context) {
        YamlContext.Schema.Wizard.Defaults.Text text = new YamlContext.Schema.Wizard.Defaults.Text();
        text.setMaxLength(String.valueOf(255));

        YamlContext.Schema.Wizard.Defaults defaults = new YamlContext.Schema.Wizard.Defaults();
        defaults.setText(text);

        YamlContext.Schema.Wizard.Database database = new YamlContext.Schema.Wizard.Database();
        database.setConnectionUrl(EnvUtils.DB_URL);
        database.setUsername(EnvUtils.DB_USERNAME);
        database.setPassword(EnvUtils.DB_PASSWORD);

        YamlContext.Schema.Wizard.Migration migration = new YamlContext.Schema.Wizard.Migration();
        migration.setPackageName(String.format("io.github.andriybosik.schemawizard.core.migration.%s", migrationType));

        YamlContext.Schema.Wizard.Extension extension = new YamlContext.Schema.Wizard.Extension();
        extension.setPackageName(String.format("io.github.andriybosik.schemawizard.core.extension.%s", migrationType));

        YamlContext.Schema.Wizard.Log log = new YamlContext.Schema.Wizard.Log();
        log.setSqlQuery(String.valueOf(true));

        YamlContext.Schema.Wizard wizard = new YamlContext.Schema.Wizard();
        wizard.setContext(context);
        wizard.setDatabase(database);
        wizard.setMigration(migration);
        wizard.setExtension(extension);
        wizard.setDefaults(defaults);
        wizard.setLog(log);
        return wizard;
    }
}
