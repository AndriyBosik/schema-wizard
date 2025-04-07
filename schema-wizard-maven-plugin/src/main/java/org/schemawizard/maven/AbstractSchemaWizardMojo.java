package org.schemawizard.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.schemawizard.core.starter.SchemaWizard;
import org.schemawizard.core.starter.SchemaWizardBuilder;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractSchemaWizardMojo extends AbstractMojo {
    private final static String CONFIGURATION_FILE_PATH = "src/main/resources/application.yaml";

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    protected MavenProject project;

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
