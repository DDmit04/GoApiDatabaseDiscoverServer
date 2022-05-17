package com.discover.dbdiscover.service.implementation.grpc;

import com.common.DatabaseIdRequest;
import com.common.DatabaseStatsResponse;
import com.discover.dbdiscover.controller.dto.database.DatabaseStatsInfo;
import com.discover.dbdiscover.service.interfaces.grpc.DatabaseApiService;
import com.discover.dbdiscover.service.interfaces.grpc.DatabaseStatsApiService;
import com.server.DatabaseServerServiceGrpc;
import com.server.DatabaseServerStatsServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
public class DatabaseStatsApiServiceImpl implements DatabaseStatsApiService {

    @Override
    public DatabaseStatsInfo getDatabaseStats(String url, Integer dbId) {
        DatabaseServerStatsServiceGrpc.DatabaseServerStatsServiceBlockingStub blockingStub = createServerStub(url);
        DatabaseIdRequest request = DatabaseIdRequest.newBuilder().setDbId(dbId).build();
        DatabaseStatsResponse response = blockingStub.getServerStats(request);
        DatabaseStatsInfo info = new DatabaseStatsInfo(response.getCurrentSize(), response.getFillPercent());
        return info;
    }

    private DatabaseServerStatsServiceGrpc.DatabaseServerStatsServiceBlockingStub createServerStub(String url) {
        ManagedChannel channel = ManagedChannelBuilder.forTarget(url)
            .usePlaintext()
            .build();
        DatabaseServerStatsServiceGrpc.DatabaseServerStatsServiceBlockingStub blockingStub =
            DatabaseServerStatsServiceGrpc.newBlockingStub(channel);
        return blockingStub;
    }

}
