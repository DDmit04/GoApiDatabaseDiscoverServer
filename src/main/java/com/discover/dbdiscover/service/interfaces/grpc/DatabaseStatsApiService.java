package com.discover.dbdiscover.service.interfaces.grpc;

import com.discover.dbdiscover.controller.dto.database.DatabaseStatsInfo;
import com.discover.dbdiscover.controller.dto.database.DatabaseUsernameInfo;

public interface DatabaseStatsApiService {

    DatabaseStatsInfo getDatabaseStats(String url, Integer dbId);
}
