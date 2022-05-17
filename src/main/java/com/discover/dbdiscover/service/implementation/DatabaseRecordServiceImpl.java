package com.discover.dbdiscover.service.implementation;

import com.discover.dbdiscover.controller.dto.database.DatabaseRecordInfo;
import com.discover.dbdiscover.domain.DatabaseRecord;
import com.discover.dbdiscover.domain.DatabaseServerRecord;
import com.discover.dbdiscover.repo.DatabaseRecordRepo;
import com.discover.dbdiscover.service.interfaces.DatabaseRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class DatabaseRecordServiceImpl implements DatabaseRecordService {

    private final DatabaseRecordRepo databaseRecordRepo;

    @Override
    public boolean isAnyDatabaseExistsByDbIds(List<Integer> dbIds) {
        return databaseRecordRepo.findAllByDbIdIn(dbIds).size() > 0;
    }

    @Override
    public Optional<DatabaseRecord> findDatabaseByDbId(Integer dbId) {
        return databaseRecordRepo.findByDbId(dbId);
    }

    @Override
    public String findDatabaseUrlByDbId(Integer dbId) {
        DatabaseServerRecord serverRecord = getDatabaseRecordById(dbId);
        return serverRecord.getUrl();
    }

    @Override
    public String findDatabaseGrpcUrlByDbId(Integer dbId) {
        DatabaseServerRecord serverRecord = getDatabaseRecordById(dbId);
        return serverRecord.getGrpcUrl();
    }

    @Override
    public List<DatabaseRecord> saveAllNewDatabases(List<DatabaseRecordInfo> databaseInfoList, DatabaseServerRecord databaseServer) {
        List<DatabaseRecord> databaseRecords = databaseInfoList
            .stream()
            .map(db -> new DatabaseRecord(db.getDbId(), db.getUsername(), db.getDatabaseName(), db.getDbSize(), databaseServer))
            .collect(Collectors.toList());
        return databaseRecordRepo.saveAll(databaseRecords);
    }

    @Override
    public void deleteDatabaseRecords(List<DatabaseRecord> databaseRecords) {
        databaseRecordRepo.deleteAll(databaseRecords);
    }

    @Override
    public void updateDatabaseAuthInfo(DatabaseRecord databaseRecord, String username, String databaseName) {
        databaseRecord.setUsername(username);
        databaseRecord.setDbName(databaseName);
        databaseRecordRepo.save(databaseRecord);
    }

    @Override
    public DatabaseRecord saveNewDatabase(DatabaseRecordInfo newDbInfo, DatabaseServerRecord server) {
        List<DatabaseRecord> databaseRecords = saveAllNewDatabases(new ArrayList<>() {{
            add(newDbInfo);
        }}, server);
        return databaseRecords.get(0);
    }

    @Override
    public void updateDatabaseSize(DatabaseRecord databaseRecord, long newSize) {
        databaseRecord.setSize(newSize);
        databaseRecordRepo.save(databaseRecord);
    }

    private DatabaseServerRecord getDatabaseRecordById(Integer dbId) {
        Optional<DatabaseRecord> databaseRecordOptional = findDatabaseByDbId(dbId);
        return databaseRecordOptional
            .map(db -> db.getServer())
            .orElseThrow(() -> new RuntimeException());
    }
}
