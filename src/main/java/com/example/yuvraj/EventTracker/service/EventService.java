package com.example.yuvraj.EventTracker.service;

import com.example.yuvraj.EventTracker.Entitiy.EventEntity;
import com.example.yuvraj.EventTracker.Entitiy.UserEntity;
import com.example.yuvraj.EventTracker.repository.EventRepository;
import com.example.yuvraj.EventTracker.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;
//    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    public void createEvent(EventEntity event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        eventRepository.save(event);
        if(auth!=null && auth.isAuthenticated()) {
           UserEntity user =  userRepository.findByUsername(auth.getName()).get();
            user.getHostedEvents().add(event.getObjectId());
            System.out.println(user.getHostedEvents());
            userRepository.save(user);
        }
    }
    public List<EventEntity> getAllEvents() {
        return eventRepository.findAll();
    }
    public List<EventEntity> getUserBookmarks() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth!=null && auth.isAuthenticated()) {
            return eventRepository.getEventsByIdList(userRepository.findByUsername(auth.getName()).get().getBookmarked());
        }
        return List.of();
    }
    public List<EventEntity> getUserHostedEvents() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth!=null && auth.isAuthenticated()) {
            List<ObjectId> HostedEventIds = Collections.emptyList();
            Optional<UserEntity> user = userRepository.findByUsername(auth.getName());
            if(user.isPresent())
                HostedEventIds = user.get().getHostedEvents();
            if(HostedEventIds == null)
                HostedEventIds = Collections.emptyList();
            return eventRepository.getEventsByIdList(HostedEventIds);
        }
        return List.of();
    }
    public void BookmarkEvent(ObjectId eventId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth!=null && auth.isAuthenticated()) {
            UserEntity user =  userRepository.findByUsername(auth.getName()).get();
            user.getBookmarked().add(eventId);
            userRepository.save(user);
        }
    }
    public void UnBookmarkEvent(ObjectId eventId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth!=null && auth.isAuthenticated()) {
            UserEntity user =  userRepository.findByUsername(auth.getName()).get();
            user.getBookmarked().remove(eventId);
            userRepository.save(user);
        }
    }
}
