package com.ayush.RestApiWithDatabase.Repo;

import com.ayush.RestApiWithDatabase.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public UserRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<User> getUserforSentimentAnalysis(){

        Query query = new Query();
//        Criteria criteria = new Criteria();

        query.addCriteria(Criteria.where("email").regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"));
        query.addCriteria(Criteria.where("sentimentAnalysis").is(true));

//        query.addCriteria(criteria.orOperator(
//                Criteria.where("email").is(true),
//                Criteria.where("sentimentAnalysis").is(true)
//        ));

        return mongoTemplate.find(query, User.class);
    }
}
