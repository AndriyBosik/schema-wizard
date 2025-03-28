package org.schemawizard.maven;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.schemawizard.core.starter.SchemaWizard;

@Mojo(
        name = "upgrade",
        defaultPhase = LifecyclePhase.PRE_INTEGRATION_TEST,
        requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class UpgradeMojo extends AbstractSchemaWizardMojo {
    @Override
    protected void doExecute(SchemaWizard schemaWizard) {
        schemaWizard.up();
    }
}
