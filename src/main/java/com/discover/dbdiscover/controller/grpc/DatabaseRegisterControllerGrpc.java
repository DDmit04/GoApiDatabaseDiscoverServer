package com.discover.dbdiscover.controller.grpc;

import com.common.ResponseResult;
import com.discover.dbdiscover.controller.dto.database.DatabaseRecordInfo;
import com.discover.dbdiscover.controller.dto.register.DatabaseServerRegisterInfo;
import com.discover.dbdiscover.controller.dto.register.DatabaseServerUnregisterInfo;
import com.discover.dbdiscover.service.interfaces.facade.DatabaseServerRegisterServiceFacade;
import com.example.DatabaseType;
import com.register.DatabaseServerRegisterRequest;
import com.register.DatabaseServerRegisterServiceGrpc;
import com.register.DatabaseServerUnregisterRequest;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Daniil Dmitrochenkov
 **/
@GrpcService
@RequiredArgsConstructor
public class DatabaseRegisterControllerGrpc extends DatabaseServerRegisterServiceGrpc.DatabaseServerRegisterServiceImplBase {

    private final DatabaseServerRegisterServiceFacade databaseServerRegisterServiceFacadeImpl;

    @Override
    public void unregisterServer(DatabaseServerUnregisterRequest request, StreamObserver<ResponseResult> responseObserver) {
        DatabaseServerUnregisterInfo unregisterInfo = new DatabaseServerUnregisterInfo(request.getDbUrl());
        boolean deleted = databaseServerRegisterServiceFacadeImpl.unregisterServer(unregisterInfo);
        ResponseResult response = ResponseResult.newBuilder().setResult(deleted).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void registerServer(DatabaseServerRegisterRequest request, StreamObserver<ResponseResult> responseObserver) {
        List<DatabaseRecordInfo> databases = request.getDatabasesList()
            .stream()
            .map(db -> new DatabaseRecordInfo(db.getUsername(), db.getDbName(), db.getDbId(), db.getSize()))
            .collect(Collectors.toList());
        DatabaseServerRegisterInfo registerInfo = new DatabaseServerRegisterInfo(
            DatabaseType.valueOf(request.getDatabaseType()),
            request.getDbUrl(),
            request.getDbGrpcUrl(),
            request.getFreeSpace(),
            databases);
        boolean saved = databaseServerRegisterServiceFacadeImpl.registerServer(registerInfo);
        ResponseResult response = ResponseResult.newBuilder().setResult(saved).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
