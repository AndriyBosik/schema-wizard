package org.schemawizard.gradle.task;

import org.schemawizard.core.starter.SchemaWizard;
import org.schemawizard.core.utils.StringUtils;
import org.schemawizard.gradle.task.metadata.ConfigConst;

import java.util.Arrays;
import java.util.Objects;

public class DowngradeTask extends AbstractSchemaWizardTask {
    @Override
    protected Object doExecute(SchemaWizard schemaWizard) {
        validateParams();
        if (StringUtils.isNotBlank(context)) {
            getLogger().info("[START] Downgrading by context: {}", context);
            schemaWizard.downByContext(context);
            getLogger().info("[FINISH] Downgrading by context: {}", context);
        } else if (version != null) {
            getLogger().info("[START] Downgrading by version: {}", version);
            schemaWizard.downByVersion(version);
            getLogger().info("[FINISH] Downgrading by version: {}", version);
        } else if (count != null) {
            getLogger().info("[START] Downgrading by count: {}", count);
            schemaWizard.downByCount(count);
            getLogger().info("[FINISH] Downgrading by count: {}", count);
        }
        return null;
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
