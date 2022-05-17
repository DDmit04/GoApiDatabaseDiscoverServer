package com.discover.dbdiscover.config;

import com.discover.ExternalDatabaseServiceGrpc;
import com.discover.dbdiscover.controller.dto.register.DatabaseServerUnregisterInfo;
import com.discover.dbdiscover.domain.DatabaseServerRecord;
import com.discover.dbdiscover.service.implementation.facade.DatabaseServerRegisterServiceFacadeImpl;
import com.discover.dbdiscover.service.interfaces.ServerRecordService;
import com.discover.dbdiscover.service.interfaces.grpc.DatabaseServerHealthCheckService;
import com.discover.ExternalDatabaseServiceStatsGrpc;
import io.grpc.health.v1.HealthCheckResponse;
import io.grpc.services.HealthStatusManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MyCommandLineRunner implements CommandLineRunner {

    private final ServerRecordService serverRecordService;
    private final DatabaseServerRegisterServiceFacadeImpl databaseServerRegisterServiceFacade;
    private final DatabaseServerHealthCheckService databaseServerHealthCheckService;
    private final HealthStatusManager healthStatusManager;

    @Override
    public void run(String... args) {
        checkActiveDatabases();
        healthStatusManager.setStatus(ExternalDatabaseServiceGrpc.getServiceDescriptor().getName(), HealthCheckResponse.ServingStatus.SERVING);
        healthStatusManager.setStatus(ExternalDatabaseServiceStatsGrpc.getServiceDescriptor().getName(), HealthCheckResponse.ServingStatus.SERVING);
    }

    private void checkActiveDatabases() {
        List<DatabaseServerRecord> allServerRecords = serverRecordService.getAllServerRecords();
        allServerRecords.forEach(rec -> {
            String serverGrpcUrl = rec.getGrpcUrl();
            String serverUrl = rec.getUrl();
            boolean serverAlive = true;
            try {
                boolean databaseServiceIsAlive = databaseServerHealthCheckService.isExternalDatabaseServiceServing(serverGrpcUrl);
                boolean databaseStatsServiceIsAlive = databaseServerHealthCheckService.isExternalDatabaseStatsServiceServing(serverGrpcUrl);
                serverAlive = databaseServiceIsAlive && databaseStatsServiceIsAlive;
            } catch (io.grpc.StatusRuntimeException e) {
                serverAlive = false;
            }
            if(!serverAlive) {
                DatabaseServerUnregisterInfo serverUnregisterInfo = new DatabaseServerUnregisterInfo(serverUrl);
                databaseServerRegisterServiceFacade.unregisterServer(serverUnregisterInfo);
            }
        });
    }
}