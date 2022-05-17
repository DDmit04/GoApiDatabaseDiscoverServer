package com.discover.dbdiscover.service.implementation.facade;

import com.discover.dbdiscover.controller.dto.database.DatabaseRecordInfo;
import com.discover.dbdiscover.controller.dto.register.DatabaseServerRegisterInfo;
import com.discover.dbdiscover.controller.dto.register.DatabaseServerUnregisterInfo;
import com.discover.dbdiscover.domain.DatabaseRecord;
import com.discover.dbdiscover.domain.DatabaseServerRecord;
import com.discover.dbdiscover.service.interfaces.DatabaseRecordService;
import com.discover.dbdiscover.service.interfaces.ServerRecordService;
import com.discover.dbdiscover.service.interfaces.facade.DatabaseServerRegisterServiceFacade;
import com.example.DatabaseType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class DatabaseServerRegisterServiceFacadeImpl implements DatabaseServerRegisterServiceFacade {

    private final DatabaseRecordService databaseRecordService;
    private final ServerRecordService serverRecordService;

    @Override
    public boolean registerServer(DatabaseServerRegisterInfo databaseServerRegisterInfo) {
        String serverUrl = databaseServerRegisterInfo.getUrl();
        String serverGrpcUrl = databaseServerRegisterInfo.getGrpcUrl();
        Optional<DatabaseServerRecord> server = serverRecordService.findByUrl(serverUrl);
        if (server.isEmpty()) {
            List<DatabaseRecordInfo> databasesInfo = databaseServerRegisterInfo
                .getDatabaseInfoList().stream()
                .map(res -> new DatabaseRecordInfo(res.getUsername(), res.getDatabaseName(), res.getDbId(), res.getDbSize()))
                .collect(Collectors.toList());
            boolean anyDbExists = databaseRecordService.isAnyDatabaseExistsByDbIds(
                databasesInfo.stream()
                    .map(db -> db.getDbId())
                    .collect(Collectors.toList()));
            if (!anyDbExists) {
                DatabaseType type = databaseServerRegisterInfo.getDatabaseType();
                long leftSpace = databaseServerRegisterInfo.getFreeSpace();
                DatabaseServerRecord databaseServer = serverRecordService.saveNewServerRecord(serverUrl, serverGrpcUrl, type, leftSpace);
                databaseRecordService.saveAllNewDatabases(databasesInfo, databaseServer);
                return true;
            } else {
                throw new RuntimeException();
            }
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public boolean unregisterServer(DatabaseServerUnregisterInfo databaseServerUnregisterInfo) {
        String serverUrl = databaseServerUnregisterInfo.getUrl();
        Optional<DatabaseServerRecord> server = serverRecordService.findByUrl(serverUrl);
        return server.map(srv -> {
                List<DatabaseRecord> databases = srv.getDatabases();
                databaseRecordService.deleteDatabaseRecords(databases);
                serverRecordService.deleteServerRecord(srv);
                return true;
            })
            .orElseThrow(() -> new RuntimeException());
    }

}
