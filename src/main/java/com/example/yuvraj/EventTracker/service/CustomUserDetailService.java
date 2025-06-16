package com.example.yuvraj.EventTracker.service;

import com.example.yuvraj.EventTracker.Entitiy.UserEntity;
import com.example.yuvraj.EventTracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isEmpty())
            throw new UsernameNotFoundException("Username Not Found");
        return User.builder().username(user.get().getUsername())
                .password(user.get().getPassword())
                .roles(String.valueOf(user.get().getRoles()))
                .build();
    }
}
