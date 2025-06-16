package com.example.yuvraj.EventTracker.repository;

import com.example.yuvraj.EventTracker.Entitiy.EventEntity;
import org.bson.types.ObjectId;

import java.util.List;

public interface EventRepositoryCustom {
    public List<EventEntity> getEventsByIdList(List<ObjectId> ids);
}
