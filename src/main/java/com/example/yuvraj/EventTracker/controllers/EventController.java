package com.example.yuvraj.EventTracker.controllers;


import com.example.yuvraj.EventTracker.Entitiy.EventEntity;
import com.example.yuvraj.EventTracker.dto.ApiResponse;
import com.example.yuvraj.EventTracker.service.CloudinaryService;
import com.example.yuvraj.EventTracker.service.EventService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
public class EventController {
    @Autowired
    private EventService eventService;
    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping(value = "/new-event", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> createEvent(
            @RequestParam("eventName") String eventName,
            @RequestParam("eventDescription") String eventDescription,
            @RequestParam("eventCategory") String eventCategory,
            @RequestParam("eventDate") String eventDate,
            @RequestParam("eventTime") String eventTime,
            @RequestParam("location") String location,
            @RequestParam("eventLink") String eventLink,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        
        EventEntity event = new EventEntity();
        event.setEventName(eventName);
        event.setEventDescription(eventDescription);
        event.setEventCategory(eventCategory);
        event.setEventDate(eventDate);
        event.setEventTime(eventTime);
        event.setLocation(location);
        event.setEventLink(eventLink);
        
        if (image != null && !image.isEmpty()) {
            String imageUrl = cloudinaryService.uploadImage(image);
            event.setEventImageUrl(imageUrl);
        }
        eventService.createEvent(event);
        ApiResponse<Void> resp = new ApiResponse<>(true, "Event Created Successfully", null);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping("/all-events")
    public ResponseEntity<ApiResponse<List<EventEntity>>> getAllEvents() {
        try {
            List<EventEntity> events = eventService.getAllEvents();
            ApiResponse<List<EventEntity>> resp =
                    new ApiResponse<>(true, "All Events Retrieved", events);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            // Log the exception as needed
            ApiResponse<List<EventEntity>> errorResp =
                    new ApiResponse<>(false,
                            "Failed to retrieve events: " + e.getMessage(),
                            Collections.emptyList());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResp);
        }
    }

    @PostMapping("/bookmark-event")
    public ResponseEntity<ApiResponse<Void>> bookmarkEvent(@RequestBody ObjectId eventId) {
        eventService.BookmarkEvent(eventId);
        ApiResponse<Void> resp = new ApiResponse<>(true, "Event Bookmarked Successfully", null);
        return ResponseEntity.ok(resp);
    }
    @PostMapping("/unbookmark-event")
    public ResponseEntity<ApiResponse<Void>> unbookmarkEvent(@RequestBody ObjectId eventId) {
        eventService.UnBookmarkEvent(eventId);
        ApiResponse<Void> resp = new ApiResponse<>(true, "Event Unbookmarked Successfully", null);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/hosted-event")
    public ResponseEntity<ApiResponse<List<EventEntity>>> getHostedEvent() {
        List<EventEntity> hosted = eventService.getUserHostedEvents();
        ApiResponse<List<EventEntity>> resp = new ApiResponse<>(true, "Hosted Events Retrieved", hosted);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/bookmarked-event")
    public ResponseEntity<ApiResponse<List<EventEntity>>> getBookmarkedEvent() {
        List<EventEntity> bookmarks = eventService.getUserBookmarks();
        ApiResponse<List<EventEntity>> resp = new ApiResponse<>(true, "Bookmarked Events Retrieved", bookmarks);
        return ResponseEntity.ok(resp);
    }
    @PostMapping("/upload-image")
    public ResponseEntity<ApiResponse<Void>> uploadImageTest(@RequestParam("image") MultipartFile image) throws IOException {
        cloudinaryService.uploadImage(image);
        ApiResponse<Void> resp = new ApiResponse<>(true, "image uploaded successfully", null);
        return ResponseEntity.ok(resp);
    }
}
