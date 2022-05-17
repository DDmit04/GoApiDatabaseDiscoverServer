package com.discover.dbdiscover.controller.dto.database;

import lombok.Getter;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class DatabaseAuthInfo extends DatabaseUsernameInfo {

    private String dbBaseUrl;

    public DatabaseAuthInfo(String username, String databaseName, String dbBaseUrl) {
        super(username, databaseName);
        this.dbBaseUrl = dbBaseUrl;
    }

    public DatabaseAuthInfo(DatabaseUsernameInfo databaseUsernameInfo, String dbBaseUrl) {
        super(databaseUsernameInfo.getUsername(), databaseUsernameInfo.getDatabaseName());
        this.dbBaseUrl = dbBaseUrl;
    }
}
