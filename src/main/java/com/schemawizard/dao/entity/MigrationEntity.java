package com.schemawizard.dao.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MigrationEntity {

    private Integer id;
    private Integer version;

    private String description;

    private Integer checksum;

    private LocalDateTime appliedOn;

    private boolean success;

}
