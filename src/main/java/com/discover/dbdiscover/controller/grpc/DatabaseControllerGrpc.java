package com.discover.dbdiscover.controller.grpc;


import com.common.DatabaseIdRequest;
import com.common.ResetDatabaseRequest;
import com.common.ResponseResult;
import com.common.SendQueryRequest;
import com.common.SendQueryResponse;
import com.common.UpdateDatabasePasswordRequest;
import com.common.UpdateDatabaseSizeRequest;
import com.discover.CreateDatabaseOnDiscoverRequest;
import com.discover.DatabaseOnDiscoverResponse;
import com.discover.ExternalDatabaseServiceGrpc;
import com.discover.dbdiscover.controller.dto.database.CreateDatabaseInfo;
import com.discover.dbdiscover.controller.dto.database.DatabaseAuthInfo;
import com.discover.dbdiscover.service.interfaces.facade.DatabaseApiServiceFacade;
import com.example.DatabaseType;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.Map;

/**
 * @author Daniil Dmitrochenkov
 **/
@GrpcService
@RequiredArgsConstructor
public class DatabaseControllerGrpc extends ExternalDatabaseServiceGrpc.ExternalDatabaseServiceImplBase {

    private final DatabaseApiServiceFacade databaseApiServiceFacadeImpl;

    @Override
    public void createDatabase(CreateDatabaseOnDiscoverRequest request, StreamObserver<DatabaseOnDiscoverResponse> responseObserver) {
        CreateDatabaseInfo createDatabaseInfo = new CreateDatabaseInfo(
            DatabaseType.valueOf(request.getDatabaseType().toString()),
            request.getDbId(),
            request.getSize(),
            request.getPassword()
        );
        DatabaseAuthInfo databaseAuthInfo = databaseApiServiceFacadeImpl.createDatabase(createDatabaseInfo);
        DatabaseOnDiscoverResponse response = DatabaseOnDiscoverResponse.newBuilder()
            .setLocation(databaseAuthInfo.getDbBaseUrl())
            .setUsername(databaseAuthInfo.getUsername())
            .setDatabaseName(databaseAuthInfo.getDatabaseName())
            .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


    @Override
    public void sendQuery(SendQueryRequest request, StreamObserver<SendQueryResponse> responseObserver) {
        Integer dbId = request.getDbId();
        String query = request.getQuery();
        SendQueryResponse queryRes = databaseApiServiceFacadeImpl.sendQuery(dbId, query);
        responseObserver.onNext(queryRes);
        responseObserver.onCompleted();
    }

    @Override
    public void dropDatabase(DatabaseIdRequest request, StreamObserver<ResponseResult> responseObserver) {
        databaseApiServiceFacadeImpl.dropDatabase(request.getDbId());
    }

    @Override
    public void resetDatabase(ResetDatabaseRequest request, StreamObserver<DatabaseOnDiscoverResponse> responseObserver) {
        DatabaseAuthInfo databaseAuthInfo = databaseApiServiceFacadeImpl.resetDatabase(request.getDbId(), request.getNewPassword());
        DatabaseOnDiscoverResponse response = DatabaseOnDiscoverResponse.newBuilder()
            .setLocation(databaseAuthInfo.getDbBaseUrl())
            .setUsername(databaseAuthInfo.getUsername())
            .setDatabaseName(databaseAuthInfo.getDatabaseName())
            .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateDatabasePassword(UpdateDatabasePasswordRequest request, StreamObserver<ResponseResult> responseObserver) {
        boolean updated = databaseApiServiceFacadeImpl.resetDatabasePassword(request.getDbId(), request.getNewPassword());
        ResponseResult response = ResponseResult.newBuilder().setResult(updated).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateDatabaseSize(UpdateDatabaseSizeRequest request, StreamObserver<ResponseResult> responseObserver) {
        boolean updated = databaseApiServiceFacadeImpl.updateDatabaseSize(request.getDbId(), request.getNewDatabaseSize());
        ResponseResult response = ResponseResult.newBuilder().setResult(updated).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
