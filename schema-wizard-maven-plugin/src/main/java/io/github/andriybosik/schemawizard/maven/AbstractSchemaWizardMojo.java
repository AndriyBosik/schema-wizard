package io.github.andriybosik.schemawizard.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.classworlds.realm.ClassRealm;
import io.github.andriybosik.schemawizard.core.starter.SchemaWizard;
import io.github.andriybosik.schemawizard.core.starter.SchemaWizardBuilder;
import io.github.andriybosik.schemawizard.maven.metadata.ConfigConst;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractSchemaWizardMojo extends AbstractMojo {
    private final static String CONFIGURATION_FILE_PATH = "src/main/resources/schema-wizard.yml";

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    protected MavenProject project;

    @Parameter(name = ConfigConst.CONTEXT)
    protected String context;

    @Parameter(name = ConfigConst.VERSION)
    protected Integer version;

    @Parameter(name = ConfigConst.COUNT)
    protected Integer count;

    @Override
    public void execute() throws MojoExecutionException {
        try {
            Set<String> classpathElements = new HashSet<>();
            classpathElements.addAll(project.getCompileClasspathElements());
            classpathElements.addAll(project.getRuntimeClasspathElements());

            ClassRealm classLoader = (ClassRealm) Thread.currentThread().getContextClassLoader();
            for (String classpathElement : classpathElements) {
                classLoader.addURL(new File(classpathElement).toURI().toURL());
            }

            File baseDir = project.getBasedir();
            File configurationFile = new File(baseDir, CONFIGURATION_FILE_PATH);

            SchemaWizard schemaWizard = SchemaWizardBuilder.init(configurationFile)
                    .classLoader(classLoader)
                    .build();

            doExecute(schemaWizard);
        } catch (Exception exception) {
            throw new MojoExecutionException(exception.getMessage(), exception);
        }
    }

    protected abstract void doExecute(SchemaWizard schemaWizard) throws MojoExecutionException, MojoFailureException;
}
