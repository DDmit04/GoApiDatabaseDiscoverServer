package com.discover.dbdiscover.service.implementation.grpc;

import com.discover.dbdiscover.service.interfaces.grpc.DatabaseServerHealthCheckService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.health.v1.HealthCheckRequest;
import io.grpc.health.v1.HealthCheckResponse;
import io.grpc.health.v1.HealthGrpc;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class DatabaseServerHealthCheckServiceImpl implements DatabaseServerHealthCheckService {


    @Value("${my.grpc.client.names.databaseServiceName}")
    private String databaseServerServiceName;
    @Value("${my.grpc.client.names.databaseStatsServiceName}")
    private String databaseServerStatsServiceName;
    @Override
    public boolean isExternalDatabaseServiceServing(String url) {
        HealthGrpc.HealthBlockingStub stub = createServerStub(url);
        HealthCheckRequest request = HealthCheckRequest.newBuilder()
            .setService(databaseServerServiceName)
            .build();
        HealthCheckResponse response = stub.check(request);
        return response.getStatus() == HealthCheckResponse.ServingStatus.SERVING;
    }

    @Override
    public boolean isExternalDatabaseStatsServiceServing(String url) {
        HealthGrpc.HealthBlockingStub stub = createServerStub(url);
        HealthCheckRequest request = HealthCheckRequest.newBuilder()
            .setService(databaseServerStatsServiceName)
            .build();
        HealthCheckResponse response = stub.check(request);
        return response.getStatus() == HealthCheckResponse.ServingStatus.SERVING;
    }

    private HealthGrpc.HealthBlockingStub createServerStub(String url) {
        ManagedChannel channel = ManagedChannelBuilder.forTarget(url)
            .usePlaintext()
            .build();
        HealthGrpc.HealthBlockingStub blockingStub = HealthGrpc.newBlockingStub(channel);
        return blockingStub;
    }

}
