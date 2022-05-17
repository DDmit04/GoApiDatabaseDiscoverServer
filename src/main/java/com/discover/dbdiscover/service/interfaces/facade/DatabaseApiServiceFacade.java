package com.discover.dbdiscover.service.interfaces.facade;

import com.common.SendQueryResponse;
import com.discover.dbdiscover.controller.dto.database.CreateDatabaseInfo;
import com.discover.dbdiscover.controller.dto.database.DatabaseAuthInfo;
import com.discover.dbdiscover.controller.dto.database.DatabaseStatsInfo;

import java.util.Map;

public interface DatabaseApiServiceFacade {
    boolean updateDatabaseSize(Integer dbId, long newSize);

    boolean resetDatabasePassword(Integer dbId, String newPassword);

    DatabaseAuthInfo resetDatabase(Integer dbId, String newPassword);

    void dropDatabase(Integer dbId);

    DatabaseAuthInfo createDatabase(CreateDatabaseInfo createDatabaseRequest);

    DatabaseStatsInfo getDatabaseStats(Integer dbId);

    SendQueryResponse sendQuery(Integer dbId, String query);
}
