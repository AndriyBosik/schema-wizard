package org.schemawizard.core.analyzer;

import java.time.LocalDateTime;

public class AppliedMigration {

    private final Integer id;
    private final Integer version;
    private final String description;
    private final LocalDateTime appliedOn;

    public AppliedMigration(Integer id, Integer version, String description, LocalDateTime appliedOn) {
        this.id = id;
        this.version = version;
        this.description = description;
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

    public LocalDateTime getAppliedOn() {
        return appliedOn;
    }
}
