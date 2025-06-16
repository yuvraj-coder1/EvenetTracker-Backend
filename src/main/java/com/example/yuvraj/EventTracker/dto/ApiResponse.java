package com.example.yuvraj.EventTracker.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ApiResponse<T> {
    private boolean success;
    private String message;
//    @JsonUnwrapped
    private T data;
    public ApiResponse(boolean success,String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
