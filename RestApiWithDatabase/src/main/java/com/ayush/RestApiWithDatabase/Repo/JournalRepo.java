package com.ayush.RestApiWithDatabase.Repo;

import com.ayush.RestApiWithDatabase.Entity.JournalEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalRepo extends MongoRepository<JournalEntity , ObjectId> {

}
