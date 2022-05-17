package com.discover.dbdiscover.controller.dto.database;

import com.example.DatabaseType;
import lombok.Getter;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class CreateDatabaseInfo {

    private DatabaseType dbType;
    private int dbId;
    private long dbSize;
    private String password;

    public CreateDatabaseInfo(DatabaseType dbType, int dbId, long dbSize, String password) {
        this.dbType = dbType;
        this.dbId = dbId;
        this.dbSize = dbSize;
        this.password = password;
    }
}
