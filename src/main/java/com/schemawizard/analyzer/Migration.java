package com.schemawizard.analyzer;

import lombok.Data;

@Data
public class Migration {
    private int version;
    private int checksum;

    private String description;

    private boolean success;
}
