package com.discover.dbdiscover.service.interfaces;

import com.discover.dbdiscover.controller.dto.database.DatabaseRecordInfo;
import com.discover.dbdiscover.domain.DatabaseRecord;
import com.discover.dbdiscover.domain.DatabaseServerRecord;

import java.util.List;
import java.util.Optional;

public interface DatabaseRecordService {
    boolean isAnyDatabaseExistsByDbIds(List<Integer> dbIds);

    Optional<DatabaseRecord> findDatabaseByDbId(Integer dbId);

    String findDatabaseUrlByDbId(Integer dbId);
    String findDatabaseGrpcUrlByDbId(Integer dbId);


    List<DatabaseRecord> saveAllNewDatabases(List<DatabaseRecordInfo> databaseInfoList, DatabaseServerRecord databaseServer);

    void deleteDatabaseRecords(List<DatabaseRecord> databaseRecords);

    void updateDatabaseAuthInfo(DatabaseRecord databaseRecord, String username, String databaseName);


    DatabaseRecord saveNewDatabase(DatabaseRecordInfo newDbInfo, DatabaseServerRecord server);

    void updateDatabaseSize(DatabaseRecord databaseRecord, long newSize);
}
