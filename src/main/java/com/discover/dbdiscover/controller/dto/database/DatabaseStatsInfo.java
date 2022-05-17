package com.discover.dbdiscover.controller.dto.database;

import lombok.Getter;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class DatabaseStatsInfo {

    private long currentSize;
    private float fillPercent;

    public DatabaseStatsInfo(long currentSize, float fillPercent) {
        this.currentSize = currentSize;
        this.fillPercent = fillPercent;
    }
}
