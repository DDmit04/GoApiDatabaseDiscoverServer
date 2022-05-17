package com.discover.dbdiscover.service.implementation.grpc;

import com.common.DatabaseIdRequest;
import com.common.ResetDatabaseRequest;
import com.common.ResponseResult;
import com.common.SendQueryRequest;
import com.common.SendQueryResponse;
import com.common.UpdateDatabasePasswordRequest;
import com.common.UpdateDatabaseSizeRequest;
import com.discover.dbdiscover.controller.dto.database.DatabaseUsernameInfo;
import com.discover.dbdiscover.service.interfaces.grpc.DatabaseApiService;
import com.server.CreateDatabaseOnServerRequest;
import com.server.DatabaseOnServerResponse;
import com.server.DatabaseServerServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
public class DatabaseApiServiceImpl implements DatabaseApiService {

    @Override
    public DatabaseUsernameInfo createDatabase(String grpcUrl, String password, Integer dbId, long maxSizeBytes) {
        DatabaseServerServiceGrpc.DatabaseServerServiceBlockingStub blockingStub = createServerStub(grpcUrl);
        CreateDatabaseOnServerRequest request = CreateDatabaseOnServerRequest.newBuilder()
            .setDbId(dbId)
            .setPassword(password)
            .setSize(maxSizeBytes)
            .build();
        DatabaseOnServerResponse response = blockingStub.createDatabase(request);
        DatabaseUsernameInfo info = new DatabaseUsernameInfo(response.getUsername(), response.getDatabaseName());
        return info;
    }

    @Override
    public DatabaseUsernameInfo resetDatabase(String url, Integer dbId, String newPassword) {
        DatabaseServerServiceGrpc.DatabaseServerServiceBlockingStub blockingStub = createServerStub(url);
        ResetDatabaseRequest request = ResetDatabaseRequest.newBuilder()
            .setDbId(dbId)
            .setNewPassword(newPassword)
            .build();
        DatabaseOnServerResponse response = blockingStub.resetDatabase(request);
        DatabaseUsernameInfo info = new DatabaseUsernameInfo(response.getUsername(), response.getDatabaseName());
        return info;
    }

    @Override
    public boolean dropDatabase(String url, Integer dbId) {
        DatabaseServerServiceGrpc.DatabaseServerServiceBlockingStub blockingStub = createServerStub(url);
        DatabaseIdRequest request = DatabaseIdRequest.newBuilder().setDbId(dbId).build();
        ResponseResult responseResult = blockingStub.dropDatabase(request);
        return responseResult.getResult();
    }

    @Override
    public boolean updateDatabaseSize(String url, Integer dbId, long newSize) {
        DatabaseServerServiceGrpc.DatabaseServerServiceBlockingStub blockingStub = createServerStub(url);
        UpdateDatabaseSizeRequest request = UpdateDatabaseSizeRequest.newBuilder()
            .setDbId(dbId)
            .setNewDatabaseSize(newSize)
            .build();
        ResponseResult responseResult = blockingStub.updateDatabaseSize(request);
        return responseResult.getResult();
    }

    @Override
    public boolean resetDatabasePassword(String url, Integer dbId, String password) {
        DatabaseServerServiceGrpc.DatabaseServerServiceBlockingStub blockingStub = createServerStub(url);
        UpdateDatabasePasswordRequest request = UpdateDatabasePasswordRequest.newBuilder()
            .setDbId(dbId)
            .setNewPassword(password)
            .build();
        ResponseResult responseResult = blockingStub.updateDatabasePassword(request);
        return responseResult.getResult();
    }

    @Override
    public SendQueryResponse execQuery(String url, Integer dbId, String query) {
        DatabaseServerServiceGrpc.DatabaseServerServiceBlockingStub blockingStub = createServerStub(url);
        SendQueryRequest request = SendQueryRequest.newBuilder()
            .setQuery(query)
            .setDbId(dbId)
            .build();
        SendQueryResponse response = blockingStub.sendQuery(request);
        return response;
    }

    private DatabaseServerServiceGrpc.DatabaseServerServiceBlockingStub createServerStub(String url) {
        ManagedChannel channel = ManagedChannelBuilder.forTarget(url)
            .usePlaintext()
            .build();
        DatabaseServerServiceGrpc.DatabaseServerServiceBlockingStub blockingStub =
            DatabaseServerServiceGrpc.newBlockingStub(channel);
        return blockingStub;
    }

}
