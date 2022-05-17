package com.discover.dbdiscover.domain;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

/**
 * @author Daniil Dmitrochenkov
 **/
@Document
@Data
public class DatabaseRecord {

    @Id
    private ObjectId id;

    @Indexed(unique = true)
    private Integer dbId;
    private String username;
    private String dbName;
    private long size;

    @DocumentReference
    private DatabaseServerRecord server;

    public DatabaseRecord(Integer dbId, String username, String dbName, long size, DatabaseServerRecord server) {
        this.dbId = dbId;
        this.username = username;
        this.dbName = dbName;
        this.size = size;
        this.server = server;
    }

    @Override
    public String toString() {
        return "DatabaseRecord{" +
            "id=" + id +
            ", dbId=" + dbId +
            ", username='" + username + '\'' +
            ", dbName='" + dbName + '\'' +
            ", size=" + size +
            ", server=" + server.getId() +
            '}';
    }
}
