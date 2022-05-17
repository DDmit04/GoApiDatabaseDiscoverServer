package com.discover.dbdiscover.service.interfaces.facade;

import com.discover.dbdiscover.controller.dto.register.DatabaseServerRegisterInfo;
import com.discover.dbdiscover.controller.dto.register.DatabaseServerUnregisterInfo;

public interface DatabaseServerRegisterServiceFacade {
    boolean registerServer(DatabaseServerRegisterInfo databaseServerRegisterInfo);

    boolean unregisterServer(DatabaseServerUnregisterInfo databaseServerUnregisterInfo);
}
