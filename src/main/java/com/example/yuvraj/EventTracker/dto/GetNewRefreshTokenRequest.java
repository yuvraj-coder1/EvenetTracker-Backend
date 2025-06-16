package com.example.yuvraj.EventTracker.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

@Data
public class GetNewRefreshTokenRequest {
    @JsonAlias("token")
    private String oldRefreshToken;
}
