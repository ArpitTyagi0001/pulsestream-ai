package com.streamanalytics.query_service.controller;

import com.streamanalytics.query_service.entity.Event;
import com.streamanalytics.query_service.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class EventController {
    private EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/all-events")
    public List<Event> allEvent(){
        return eventService.getEvent();
    }

    @GetMapping("/get/{eventId}")
    public ResponseEntity<Event> EventById(@PathVariable Long eventId){
        return ResponseEntity.ok(eventService.getEventId(eventId));
    }

    @GetMapping("/total-events")
    public long totalEvent(){
        return eventService.count();
    }

    @GetMapping("/latest-events")
    public List<Event> latestEvent(){
        return  eventService.getLatest();
    }

    @GetMapping("/location-count")
    public Map<String , Long> locationCount(){
       return eventService.countLocation();
    }

}
