package com.discover.dbdiscover.service.implementation.facade;

import com.common.SendQueryResponse;
import com.discover.dbdiscover.controller.dto.database.CreateDatabaseInfo;
import com.discover.dbdiscover.controller.dto.database.DatabaseAuthInfo;
import com.discover.dbdiscover.controller.dto.database.DatabaseRecordInfo;
import com.discover.dbdiscover.controller.dto.database.DatabaseStatsInfo;
import com.discover.dbdiscover.controller.dto.database.DatabaseUsernameInfo;
import com.discover.dbdiscover.domain.DatabaseRecord;
import com.discover.dbdiscover.domain.DatabaseServerRecord;
import com.discover.dbdiscover.service.interfaces.grpc.DatabaseApiService;
import com.discover.dbdiscover.service.interfaces.DatabaseRecordService;
import com.discover.dbdiscover.service.interfaces.ServerRecordService;
import com.discover.dbdiscover.service.interfaces.facade.DatabaseApiServiceFacade;
import com.discover.dbdiscover.service.interfaces.grpc.DatabaseStatsApiService;
import com.example.DatabaseType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class DatabaseApiServiceFacadeImpl implements DatabaseApiServiceFacade {

    private final DatabaseApiService databaseApiService;
    private final DatabaseStatsApiService databaseStatsApiService;

    private final DatabaseRecordService databaseRecordService;
    private final ServerRecordService serverRecordService;

    @Override
    public boolean updateDatabaseSize(Integer dbId, long newSize) {
        Optional<DatabaseRecord> databaseRecordOptional = databaseRecordService.findDatabaseByDbId(dbId);
        return databaseRecordOptional.map(databaseRecord -> {
            DatabaseServerRecord server = databaseRecord.getServer();
            String databaseGrpcUrl = server.getGrpcUrl();
            boolean updated = databaseApiService.updateDatabaseSize(databaseGrpcUrl, dbId, newSize);
            if (updated) {
                serverRecordService.updateServerFreeSpace(databaseRecord, server, newSize);
                databaseRecordService.updateDatabaseSize(databaseRecord, newSize);
            }
            return updated;
        }).orElseThrow(() -> new RuntimeException());
    }

    @Override
    public boolean resetDatabasePassword(Integer dbId, String newPassword) {
        Optional<DatabaseRecord> databaseRecordOptional = databaseRecordService.findDatabaseByDbId(dbId);
        return databaseRecordOptional.map(databaseRecord -> {
            DatabaseServerRecord server = databaseRecord.getServer();
            String databaseGrpcUrl = server.getGrpcUrl();
            return databaseApiService.resetDatabasePassword(databaseGrpcUrl, dbId, newPassword);
        }).orElseThrow(() -> new RuntimeException());
    }

    @Override
    public DatabaseAuthInfo resetDatabase(Integer dbId, String newPassword) {
        Optional<DatabaseRecord> databaseOptional = databaseRecordService.findDatabaseByDbId(dbId);
        return databaseOptional.map(databaseRecord -> {
            DatabaseServerRecord server = databaseRecord.getServer();
            String serverGrpcUrl = server.getGrpcUrl();
            DatabaseUsernameInfo newDatabaseUsernameInfo = databaseApiService.resetDatabase(serverGrpcUrl, dbId, newPassword);
            String newUsername = newDatabaseUsernameInfo.getUsername();
            String newDbName = newDatabaseUsernameInfo.getDatabaseName();
            databaseRecordService.updateDatabaseAuthInfo(databaseRecord, newUsername, newDbName);
            long size = databaseRecord.getSize();
            DatabaseType dbType = server.getDatabaseType();
            CreateDatabaseInfo createDatabaseInfo = new CreateDatabaseInfo(dbType, dbId, size, newPassword);
            return createDatabase(createDatabaseInfo);
        }).orElseThrow(() -> new RuntimeException());
    }

    @Override
    public void dropDatabase(Integer dbId) {
        Optional<DatabaseRecord> databaseRecordOptional = databaseRecordService.findDatabaseByDbId(dbId);
        databaseRecordOptional.map(databaseRecord -> {
            dropExistingDatabase(databaseRecord);
            return databaseRecord;
        }).orElseThrow(() -> new RuntimeException());
    }

    @Override
    public DatabaseAuthInfo createDatabase(CreateDatabaseInfo createDatabaseInfo) {
        int dbId = createDatabaseInfo.getDbId();
        String password = createDatabaseInfo.getPassword();
        long size = createDatabaseInfo.getDbSize();
        DatabaseType type = createDatabaseInfo.getDbType();
        Optional<DatabaseServerRecord> bestServer = serverRecordService.findBestDatabaseServer(type, size);
        return bestServer.map(server -> {
            String serverUrl = server.getUrl();
            String grpcServerUrl = server.getGrpcUrl();
            Optional<DatabaseRecord> databaseRecordOptional = databaseRecordService.findDatabaseByDbId(dbId);
            if (databaseRecordOptional.isEmpty()) {
                DatabaseUsernameInfo databaseUsernameInfo = databaseApiService.createDatabase(grpcServerUrl, password, dbId, size);
                DatabaseAuthInfo dbAuthInfo = new DatabaseAuthInfo(databaseUsernameInfo, serverUrl);
                DatabaseRecordInfo dbInfoExtended = new DatabaseRecordInfo(dbId, size, dbAuthInfo);
                DatabaseRecord databaseRecord = databaseRecordService.saveNewDatabase(dbInfoExtended, server);
                serverRecordService.takeServerLeftSpace(databaseRecord, server);
                return dbAuthInfo;
            }
            throw new RuntimeException();
        }).orElseThrow(() -> new RuntimeException());
    }

    @Override
    public DatabaseStatsInfo getDatabaseStats(Integer dbId) {
        String databaseGrpcUrl = databaseRecordService.findDatabaseGrpcUrlByDbId(dbId);
        if (databaseGrpcUrl != null) {
            return databaseStatsApiService.getDatabaseStats(databaseGrpcUrl, dbId);
        }
        throw new RuntimeException();
    }

    @Override
    public SendQueryResponse sendQuery(Integer dbId, String query) {
        String databaseGrpcUrl = databaseRecordService.findDatabaseGrpcUrlByDbId(dbId);
        if (databaseGrpcUrl != null) {
            return databaseApiService.execQuery(databaseGrpcUrl, dbId, query);
        }
        throw new RuntimeException();
    }

    private void dropExistingDatabase(DatabaseRecord databaseRecord) {
        DatabaseServerRecord server = databaseRecord.getServer();
        String databaseGrpcUrl = server.getGrpcUrl();
        databaseRecordService.deleteDatabaseRecords(new ArrayList<>() {{
            add(databaseRecord);
        }});
        serverRecordService.freeServerLeftSpace(databaseRecord, server);
        databaseApiService.dropDatabase(databaseGrpcUrl, databaseRecord.getDbId());
    }
}
