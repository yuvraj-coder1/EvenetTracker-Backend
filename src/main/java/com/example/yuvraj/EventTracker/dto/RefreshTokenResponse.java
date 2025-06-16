package com.example.yuvraj.EventTracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenResponse {
//    @JsonProperty("refresh-token")
    private final String refreshToken;
    private final String accessToken;

    public RefreshTokenResponse(String refreshToken, String accessToken) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }
}

