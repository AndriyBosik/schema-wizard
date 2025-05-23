package io.github.andriybosik.schemawizard.gradle.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ResolvedArtifact;
import org.gradle.api.artifacts.ResolvedConfiguration;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.SourceSetOutput;
import org.gradle.api.tasks.TaskAction;
import io.github.andriybosik.schemawizard.core.starter.SchemaWizard;
import io.github.andriybosik.schemawizard.core.starter.SchemaWizardBuilder;
import io.github.andriybosik.schemawizard.gradle.task.metadata.ConfigConst;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractSchemaWizardTask extends DefaultTask {
    private final static String CONFIGURATION_FILE_PATH = "src/main/resources/schema-wizard.yml";
    private final static Set<String> GRADLE_CONFIGURATIONS = Set.of("compileClasspath", "runtimeClasspath");

    protected String context;
    protected Integer version;
    protected Integer count;

    @TaskAction
    public Object execute() {
        ClassLoader parentClassLoader = getProject().getBuildscript().getClassLoader();
        URL[] urls = Stream.concat(
                        mergeClassesAndResources(),
                        getArtifacts())
                .filter(Objects::nonNull)
                .collect(Collectors.toSet())
                .toArray(URL[]::new);

        URLClassLoader classLoader = new URLClassLoader(urls, parentClassLoader);
        File configurationFile = new File(getProject().getProjectDir().getAbsolutePath(), CONFIGURATION_FILE_PATH);

        initProperties();

        SchemaWizard schemaWizard = SchemaWizardBuilder.init(configurationFile)
                .classLoader(classLoader)
                .build();

        return doExecute(schemaWizard);
    }

    protected abstract Object doExecute(SchemaWizard schemaWizard);

    private Stream<URL> mergeClassesAndResources() {
        return getProject().getExtensions().getByType(SourceSetContainer.class).stream()
                .flatMap(this::getClassesAndResources);
    }

    private Stream<URL> getClassesAndResources(SourceSet sourceSet) {
        return Stream.concat(
                getResources(sourceSet),
                getClasses(sourceSet));
    }

    private Stream<URL> getClasses(SourceSet sourceSet) {
        return Stream.concat(
                        sourceSet.getOutput().getClassesDirs().getFiles().stream(),
                        sourceSet.getRuntimeClasspath().getFiles().stream())
                .map(this::getUrlOrNullQuietly);
    }

    private Stream<URL> getResources(SourceSet sourceSet) {
        return Stream.concat(
                        Optional.of(sourceSet.getOutput())
                                .map(SourceSetOutput::getResourcesDir)
                                .stream(),
                        sourceSet.getResources().getFiles().stream())
                .map(this::getUrlOrNullQuietly);
    }

    private Stream<URL> getArtifacts() {
        return GRADLE_CONFIGURATIONS.stream()
                .map(configuration -> getProject().getConfigurations().getByName(configuration))
                .map(Configuration::getResolvedConfiguration)
                .map(ResolvedConfiguration::getResolvedArtifacts)
                .flatMap(Set::stream)
                .map(ResolvedArtifact::getFile)
                .map(this::getUrlOrNullQuietly);
    }

    private URL getUrlOrNullQuietly(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException exception) {
            getLogger().error("Unable to determine file's URL: {}", file.getAbsolutePath(), exception);
        }
        return null;
    }

    private void initProperties() {
        Project project = getProject();
        context = getStringValue(project, ConfigConst.CONTEXT);
        version = getIntegerValue(project, ConfigConst.VERSION);
        count = getIntegerValue(project, ConfigConst.COUNT);
    }

    private String getStringValue(Project project, String propertyName) {
        Object property = project.findProperty(propertyName);
        return property != null ? property.toString() : null;
    }

    private Integer getIntegerValue(Project project, String propertyName) {
        String rawValue = getStringValue(project, propertyName);
        if (rawValue == null) {
            return null;
        }
        try {
            return Integer.parseInt(rawValue);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Unable to parse value for property '" + propertyName + "': " + rawValue + " (must be an integer)");
        }
    }
}
