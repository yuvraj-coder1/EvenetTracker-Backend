package com.example.yuvraj.EventTracker.service;

import com.example.yuvraj.EventTracker.Entitiy.UserEntity;
import com.example.yuvraj.EventTracker.dto.UserLogInRequest;
import com.example.yuvraj.EventTracker.repository.UserRepository;
import com.example.yuvraj.EventTracker.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailService userDetailService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void signUp(UserEntity user) throws Exception {
        log.info("Attempting to sign up user: {}", user.getUsername());
        try {
            user.setRoles(List.of("USER"));
            user.setDate(LocalDateTime.now());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            log.info("Successfully signed up user: {}", user.getUsername());
        } catch (Exception e) {
            log.error("Failed to sign up user: {}. Error: {}", user.getUsername(), e.getMessage(), e);
            throw e;
        }
    }

    public Pair<String, String> logIn(UserLogInRequest user) throws AuthenticationException {
        log.info("Attempting to log in user: {}", user.getUsername());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
            String jwt = jwtUtil.generateToken(user.getUsername());
            String refreshToken = jwtUtil.generateToken(user.getUsername(),30L*24*3600*1000L,"refresh");
            log.info("Successfully logged in user: {}", user.getUsername());
            return Pair.of(jwt, refreshToken);
        } catch (AuthenticationException e) {
            log.error("Failed to log in user: {}. Error: {}", user.getUsername(), e.getMessage());
            throw e;
        }
    }

    public UserEntity getUserById(ObjectId userId) {
        log.debug("Fetching user by ID: {}", userId);
        try {
            Optional<UserEntity> user = userRepository.findById(userId);
            if (user.isPresent()) {
                log.debug("Found user: {} for ID: {}", user.get().getUsername(), userId);
                return user.get();
            } else {
                log.warn("No user found with ID: {}", userId);
                throw new UsernameNotFoundException("User Does not Exist");
            }
        } catch (Exception e) {
            log.error("Error fetching user by ID: {}. Error: {}", userId, e.getMessage(), e);
            throw e;
        }
    }

    public String getRefreshToken(String oldRefreshToken) {
        log.debug("Generating new refresh token from old token");
        try {
            String username = jwtUtil.extractUsername(oldRefreshToken);
            String newRefreshToken = jwtUtil.generateToken(username, 30L * 24 * 3600 * 1000L, "refresh");
            log.info("Successfully generated new refresh token for user: {}", username);
            return newRefreshToken;
        } catch (Exception e) {
            log.error("Failed to generate new refresh token. Error: {}", e.getMessage(), e);
            throw e;
        }
    }

    public String getAccessToken(String oldRefreshToken) {
        log.debug("Generating new access token from refresh token");
        try {
            String username = jwtUtil.extractUsername(oldRefreshToken);
            String accessToken = jwtUtil.generateToken(username);
            log.info("Successfully generated new access token for user: {}", username);
            return accessToken;
        } catch (Exception e) {
            log.error("Failed to generate new access token. Error: {}", e.getMessage(), e);
            throw e;
        }
    }

    public String getUserIdByName(String username) {
        log.debug("Fetching user ID for username: {}", username);
        try {
            Optional<UserEntity> user = userRepository.findByUsername(username);
            if (user.isPresent()) {
                log.debug("Found user ID {} for username: {}", user.get().getId(), username);
                return user.get().getId().toHexString();
            } else {
                log.warn("No user found with username: {}", username);
                throw new UsernameNotFoundException("User Does not Exist");
            }
        } catch (Exception e) {
            log.error("Error fetching user ID for username: {}. Error: {}", username, e.getMessage(), e);
            throw e;
        }
    }
}
