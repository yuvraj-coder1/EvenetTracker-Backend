package com.example.yuvraj.EventTracker.Entitiy;

import lombok.Data;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
public class UserEntity {
    @Id
    private ObjectId id;
    @NonNull
    @Indexed(unique = true)
    private String username;
    @NonNull
    private String password;
    private LocalDateTime date;
    private List<String> roles;
    private String collegeId;
    @NonNull
    private String email;
    private List<ObjectId> hostedEvents = new ArrayList<>();
    private List<ObjectId> bookmarked = new ArrayList<>();
}
