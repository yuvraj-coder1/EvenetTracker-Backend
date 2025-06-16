package com.example.yuvraj.EventTracker.repository;

import com.example.yuvraj.EventTracker.Entitiy.EventEntity;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class EventRepositoryImpl implements EventRepositoryCustom{
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public List<EventEntity> getEventsByIdList(List<ObjectId> ids) {
        return mongoTemplate.find(
                Query.query(Criteria.where("_id").in(ids)),
                EventEntity.class,
                "events"
        );
    }
}
