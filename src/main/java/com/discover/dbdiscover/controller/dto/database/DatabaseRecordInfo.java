package com.discover.dbdiscover.controller.dto.database;

import lombok.Getter;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class DatabaseRecordInfo extends DatabaseUsernameInfo {

    private int dbId;
    private long dbSize;

    public DatabaseRecordInfo(String username, String databaseName, int dbId, long dbSize) {
        super(username, databaseName);
        this.dbId = dbId;
        this.dbSize = dbSize;
    }

    public DatabaseRecordInfo(int dbId, long dbSize, DatabaseAuthInfo databaseAuthInfo) {
        super(databaseAuthInfo.getUsername(), databaseAuthInfo.getDatabaseName());
        this.dbId = dbId;
        this.dbSize = dbSize;
    }
}
