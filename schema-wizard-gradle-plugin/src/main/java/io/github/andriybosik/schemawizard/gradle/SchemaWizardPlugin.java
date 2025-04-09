package io.github.andriybosik.schemawizard.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import io.github.andriybosik.schemawizard.gradle.task.DowngradeTask;
import io.github.andriybosik.schemawizard.gradle.task.UpgradeTask;

public class SchemaWizardPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getTasks().register("upgrade", UpgradeTask.class);
        project.getTasks().register("downgrade", DowngradeTask.class);
    }
}
