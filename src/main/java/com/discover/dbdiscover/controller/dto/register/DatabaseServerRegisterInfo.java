package com.discover.dbdiscover.controller.dto.register;

import com.discover.dbdiscover.controller.dto.database.DatabaseRecordInfo;
import com.example.DatabaseType;
import lombok.Getter;

import java.util.List;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class DatabaseServerRegisterInfo {

    private DatabaseType databaseType;
    private String url;
    private String grpcUrl;
    private long freeSpace;
    private List<DatabaseRecordInfo> databaseInfoList;

    public DatabaseServerRegisterInfo(DatabaseType databaseType, String url, String grpcUrl, long freeSpace, List<DatabaseRecordInfo> databaseInfoList) {
        this.databaseType = databaseType;
        this.url = url;
        this.grpcUrl = grpcUrl;
        this.freeSpace = freeSpace;
        this.databaseInfoList = databaseInfoList;
    }
}
