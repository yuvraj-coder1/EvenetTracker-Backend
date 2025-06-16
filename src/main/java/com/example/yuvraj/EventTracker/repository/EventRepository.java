package com.example.yuvraj.EventTracker.repository;

import com.example.yuvraj.EventTracker.Entitiy.EventEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepository extends MongoRepository<EventEntity, ObjectId>,EventRepositoryCustom {

}
