package com.discover.dbdiscover.service.interfaces.grpc;

public interface DatabaseServerHealthCheckService {
    boolean isExternalDatabaseServiceServing(String url);

    boolean isExternalDatabaseStatsServiceServing(String url);
}
