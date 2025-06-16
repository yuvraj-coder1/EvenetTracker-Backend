package com.example.yuvraj.EventTracker.dto;

import lombok.Data;

@Data
public class GetUserDetailResponse {
    private String username;
    private String email;
    private String collegeId;
}
