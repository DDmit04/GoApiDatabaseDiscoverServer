package com.discover.dbdiscover.domain;

import com.example.DatabaseType;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Daniil Dmitrochenkov
 **/

@Document
@Data
public class DatabaseServerRecord {

    @Id
    private ObjectId id;

    @Indexed(unique=true)
    private String url;

    @Indexed(unique=true)
    private String grpcUrl;

    @ReadOnlyProperty
    @DocumentReference(lookup="{'server':?#{#self._id} }")
    private List<DatabaseRecord> databases = new ArrayList<>();
    private DatabaseType databaseType;

    private long freeSpace;


    public DatabaseServerRecord(String url, String grpcUrl, DatabaseType databaseType, long freeSpace) {
        this.url = url;
        this.grpcUrl = grpcUrl;
        this.databaseType = databaseType;
        this.freeSpace = freeSpace;
    }

    @Override
    public String toString() {
        List<String> ids = databases.stream().map(d -> d.getId()).map(id -> id.toString()).collect(Collectors.toList());
        String dbIdsString = String.join(",", ids);
        return "DatabaseServerRecord{" +
            "id=" + id +
            ", url='" + url + '\'' +
            ", databases=" + '[' + dbIdsString + ']' +
            ", databaseType=" + databaseType +
            ", freeSpace=" + freeSpace +
            '}';
    }
}
