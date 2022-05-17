package com.discover.dbdiscover.controller.dto.register;

import lombok.Getter;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class DatabaseServerUnregisterInfo {

    private String url;

    public DatabaseServerUnregisterInfo(String url) {
        this.url = url;
    }
}
