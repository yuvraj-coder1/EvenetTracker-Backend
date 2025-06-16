package com.example.yuvraj.EventTracker.controllers;

import com.example.yuvraj.EventTracker.Entitiy.UserEntity;
import com.example.yuvraj.EventTracker.dto.*;
import com.example.yuvraj.EventTracker.service.UserService;
import lombok.val;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/public/signup")
    public ResponseEntity<ApiResponse<Void>> signUp(@RequestBody UserEntity user) throws Exception {
        try {
//            System.out.println("Here");
            userService.signUp(user);
            ApiResponse<Void> response = new ApiResponse<>(true, "User Created Successfully", null);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {

            ApiResponse<Void> response = new ApiResponse<>(false, "User not created" + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/public/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody UserLogInRequest user) {
        try {
            Pair<String,String> tokens = userService.logIn(user);
            String accessToken  = tokens.getFirst();
            String refreshToken = tokens.getSecond();
            Map<String, Object> payload = new HashMap<>();

            payload.put("access_token", accessToken);
            payload.put("refresh_token", refreshToken);
            payload.put("userId",userService.getUserIdByName(user.getUsername()));
            ApiResponse<Map<String, Object>> response = new ApiResponse<>(true, "Log in Successfully", payload);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (AuthenticationException e) {
            ApiResponse<String> resp = new ApiResponse<>(false,
                    "Invalid credentials: " + e.getMessage(),
                    null);
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(resp);
        }
    }
    @PostMapping("/user-detail")
    public ResponseEntity<?> getUserDetail(@RequestBody GetUserDetailRequest userId) {
        System.out.println("Received userId = " + userId);
        try {
            UserEntity user = userService.getUserById(new ObjectId(userId.getUserId()));
            GetUserDetailResponse resp = new GetUserDetailResponse();
            resp.setUsername(user.getUsername());
            resp.setEmail(user.getEmail());
            resp.setCollegeId(user.getCollegeId());
            return ResponseEntity.status(HttpStatus.OK).body(resp);
        } catch (UsernameNotFoundException e) {
            ApiResponse<Void> resp = new ApiResponse<Void>(false,"User not found",null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
        }
    }
    @PostMapping("/auth/refresh-token")
    public ResponseEntity<RefreshTokenResponse> getNewRefreshToken(@RequestBody GetNewRefreshTokenRequest oldRefreshToken) {
        String refreshToken = userService.getRefreshToken(oldRefreshToken.getOldRefreshToken());
        String accessToken = userService.getAccessToken(oldRefreshToken.getOldRefreshToken());
        RefreshTokenResponse response = new RefreshTokenResponse(refreshToken,accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
