package com.discover.dbdiscover.repo;

import com.discover.dbdiscover.domain.DatabaseRecord;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Daniil Dmitrochenkov
 **/
public interface DatabaseRecordRepo extends MongoRepository<DatabaseRecord, ObjectId> {

    Optional<DatabaseRecord> findByDbId(Integer dbId);
    List<DatabaseRecord> findAllByDbIdIn(List<Integer> dbId);
}
