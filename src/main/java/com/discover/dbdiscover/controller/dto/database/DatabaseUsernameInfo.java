package com.discover.dbdiscover.controller.dto.database;

import lombok.Getter;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class DatabaseUsernameInfo {

    private String username;
    private String databaseName;

    public DatabaseUsernameInfo(String username, String databaseName) {
        this.username = username;
        this.databaseName = databaseName;
    }
}
