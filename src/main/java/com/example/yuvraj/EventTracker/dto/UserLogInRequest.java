package com.example.yuvraj.EventTracker.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserLogInRequest {
    @NonNull
    private String username;
    @NonNull
    private String password;
}
