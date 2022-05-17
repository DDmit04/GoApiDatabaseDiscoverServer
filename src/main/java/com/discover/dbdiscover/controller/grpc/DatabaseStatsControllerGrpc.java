package com.discover.dbdiscover.controller.grpc;

import com.common.DatabaseIdRequest;
import com.common.DatabaseStatsResponse;
import com.discover.DatabaseLocationResponse;
import com.discover.ExternalDatabaseServiceStatsGrpc;
import com.discover.dbdiscover.controller.dto.database.DatabaseStatsInfo;
import com.discover.dbdiscover.service.interfaces.DatabaseRecordService;
import com.discover.dbdiscover.service.interfaces.facade.DatabaseApiServiceFacade;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

/**
 * @author Daniil Dmitrochenkov
 **/
@GrpcService
@RequiredArgsConstructor
public class DatabaseStatsControllerGrpc extends ExternalDatabaseServiceStatsGrpc.ExternalDatabaseServiceStatsImplBase {

    private final DatabaseRecordService databaseRecordServiceImpl;
    private final DatabaseApiServiceFacade databaseApiServiceFacadeImpl;

    @Override
    public void getDatabaseLocation(DatabaseIdRequest request, StreamObserver<DatabaseLocationResponse> responseObserver) {
        String url = databaseRecordServiceImpl.findDatabaseUrlByDbId(request.getDbId());
        DatabaseLocationResponse response = DatabaseLocationResponse.newBuilder()
            .setLocation(url)
            .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getDatabaseStats(DatabaseIdRequest request, StreamObserver<DatabaseStatsResponse> responseObserver) {
        DatabaseStatsInfo info = databaseApiServiceFacadeImpl.getDatabaseStats(request.getDbId());
        DatabaseStatsResponse response = DatabaseStatsResponse.newBuilder()
            .setCurrentSize(info.getCurrentSize())
            .setFillPercent(info.getFillPercent())
            .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
