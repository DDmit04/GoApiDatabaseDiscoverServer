package com.discover.dbdiscover.service.implementation;

import com.discover.dbdiscover.domain.DatabaseRecord;
import com.discover.dbdiscover.domain.DatabaseServerRecord;
import com.discover.dbdiscover.repo.ServerRecordRepo;
import com.discover.dbdiscover.service.interfaces.ServerRecordService;
import com.example.DatabaseType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class ServerRecordServiceImpl implements ServerRecordService {

    private final ServerRecordRepo serverRecordRepo;

    @Override
    public Optional<DatabaseServerRecord> findBestDatabaseServer(DatabaseType databaseType, long minSize) {
        List<DatabaseServerRecord> serverRecords = serverRecordRepo.findServersForDatabase(databaseType, minSize);
        return serverRecords.stream().findFirst();
    }
    @Override
    public Optional<DatabaseServerRecord> findByUrl(String url) {
        return serverRecordRepo.findByUrl(url);
    }

    @Override
    public List<DatabaseServerRecord> getAllServerRecords() {
        return serverRecordRepo.findAll();
    }

    @Override
    public void deleteServerRecord(DatabaseServerRecord databaseServerRecord) {
        serverRecordRepo.delete(databaseServerRecord);
    }

    @Override
    public DatabaseServerRecord saveNewServerRecord(String url, String grpcUrl, DatabaseType databaseType, long freeSpace) {
        DatabaseServerRecord newServer = new DatabaseServerRecord(url, grpcUrl, databaseType, freeSpace);
        return serverRecordRepo.save(newServer);
    }

    @Override
    public void takeServerLeftSpace(DatabaseRecord databaseRecord, DatabaseServerRecord databaseServer) {
        databaseServer.setFreeSpace(databaseServer.getFreeSpace() - databaseRecord.getSize());
        serverRecordRepo.save(databaseServer);
    }
    @Override
    public void freeServerLeftSpace(DatabaseRecord databaseRecord, DatabaseServerRecord databaseServer) {
        databaseServer.setFreeSpace(databaseServer.getFreeSpace() + databaseRecord.getSize());
        serverRecordRepo.save(databaseServer);
    }

    @Override
    public void updateServerFreeSpace(DatabaseRecord databaseRecord, DatabaseServerRecord server, long newSize) {
        long finalSpace = server.getFreeSpace() + databaseRecord.getSize() - newSize;
        server.setFreeSpace(finalSpace);
        serverRecordRepo.save(server);
    }

}
