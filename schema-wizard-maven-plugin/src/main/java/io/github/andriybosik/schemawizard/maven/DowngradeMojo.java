package io.github.andriybosik.schemawizard.maven;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import io.github.andriybosik.schemawizard.core.starter.SchemaWizard;
import io.github.andriybosik.schemawizard.core.utils.StringUtils;
import io.github.andriybosik.schemawizard.maven.metadata.ConfigConst;

import java.util.Arrays;
import java.util.Objects;

@Mojo(
        name = "downgrade",
        defaultPhase = LifecyclePhase.PRE_INTEGRATION_TEST,
        requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class DowngradeMojo extends AbstractSchemaWizardMojo {
    @Override
    protected void doExecute(SchemaWizard schemaWizard) {
        validateParams();
        if (StringUtils.isNotBlank(context)) {
            getLog().info(
                    String.format(
                            "[START] Downgrading by context: %s",
                            context));
            schemaWizard.downByContext(context);
            getLog().info(
                    String.format(
                            "[FINISH] Downgrading by context: %s",
                            context));
        } else if (version != null) {
            getLog().info(
                    String.format(
                            "[START] Downgrading by version: %s",
                            version));
            schemaWizard.downByVersion(version);
            getLog().info(
                    String.format(
                            "[FINISH] Downgrading by version: %s",
                            version));
        } else if (count != null) {
            getLog().info(
                    String.format(
                            "[START] Downgrading by count: %s",
                            count));
            schemaWizard.downByCount(count);
            getLog().info(
                    String.format(
                            "[FINISH] Downgrading by count: %s",
                            count));
        }
    }

    private void validateParams() {
        Object[] params = {context, version, count};
        long providedParamsCount = Arrays.stream(params)
                .filter(Objects::nonNull)
                .count();
        if (providedParamsCount == 0) {
            throw new IllegalArgumentException(
                    String.format(
                            "One of the following parameters must be specified: %s, %s, or %s",
                            ConfigConst.CONTEXT,
                            ConfigConst.VERSION,
                            ConfigConst.COUNT));
        } else if (providedParamsCount > 1) {
            throw new IllegalArgumentException(
                    String.format(
                            "Only one of the following parameters is allowed to specify: %s, %s, or %s",
                            ConfigConst.CONTEXT,
                            ConfigConst.VERSION,
                            ConfigConst.COUNT));
        }
    }
}
