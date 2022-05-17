package com.discover.dbdiscover.service.interfaces;

import com.discover.dbdiscover.domain.DatabaseRecord;
import com.discover.dbdiscover.domain.DatabaseServerRecord;
import com.example.DatabaseType;

import java.util.List;
import java.util.Optional;

public interface ServerRecordService {
    Optional<DatabaseServerRecord> findBestDatabaseServer(DatabaseType databaseType, long minSize);

    Optional<DatabaseServerRecord> findByUrl(String url);

    List<DatabaseServerRecord> getAllServerRecords();

    void deleteServerRecord(DatabaseServerRecord databaseServerRecord);

    void takeServerLeftSpace(DatabaseRecord databaseRecord, DatabaseServerRecord databaseServer);

    DatabaseServerRecord saveNewServerRecord(String url, String grpcUrl, DatabaseType databaseType, long freeSpace);

    void freeServerLeftSpace(DatabaseRecord databaseRecord, DatabaseServerRecord server);

    void updateServerFreeSpace(DatabaseRecord databaseRecord, DatabaseServerRecord server, long newSize);
}
