package com.example.yuvraj.EventTracker.Entitiy;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "events")
public class EventEntity {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId objectId;
    private String eventName;
    private String eventDescription;
    private String eventCategory;
    private String eventDate;
    private String eventTime;
    private String location;
    private String eventLink;
    private String eventImageUrl;
}
