package com.discover.dbdiscover.service.interfaces.grpc;

import com.common.SendQueryResponse;
import com.discover.dbdiscover.controller.dto.database.DatabaseUsernameInfo;

import java.util.Map;

public interface DatabaseApiService {
    DatabaseUsernameInfo createDatabase(String url, String password, Integer dbId, long maxSizeBytes);

    DatabaseUsernameInfo resetDatabase(String url, Integer dbId, String newPassword);

    boolean dropDatabase(String url, Integer dbId);

    boolean updateDatabaseSize(String url, Integer dbId, long newSize);

    boolean resetDatabasePassword(String url, Integer dbId, String password);

    SendQueryResponse execQuery(String databaseUrl, Integer dbId, String query);
}
