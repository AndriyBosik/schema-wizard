package io.github.andriybosik.schemawizard.core.analyzer;

import java.time.LocalDateTime;

public class AppliedMigration {
    private final Integer id;
    private final Integer version;
    private final String description;
    private final String context;
    private final LocalDateTime appliedOn;

    public AppliedMigration(Integer id, Integer version, String description, String context, LocalDateTime appliedOn) {
        this.id = id;
        this.version = version;
        this.description = description;
        this.context = context;
        this.appliedOn = appliedOn;
    }

    public Integer getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }

    public String getContext() {
        return context;
    }

    public LocalDateTime getAppliedOn() {
        return appliedOn;
    }
}
