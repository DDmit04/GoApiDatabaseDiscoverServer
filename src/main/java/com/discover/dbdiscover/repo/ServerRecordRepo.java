package com.discover.dbdiscover.repo;

import com.discover.dbdiscover.domain.DatabaseServerRecord;
import com.example.DatabaseType;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * @author Daniil Dmitrochenkov
 **/
public interface ServerRecordRepo extends MongoRepository<DatabaseServerRecord, ObjectId> {

    Optional<DatabaseServerRecord> findByUrl(String url);

    @Query(
        value = "{'databaseType': ?0, 'freeSpace': {$gt:  ?1} }",
        sort = "{'freeSpace': -1}"
    )
    List<DatabaseServerRecord> findServersForDatabase(DatabaseType databaseType, long minSize);
}
