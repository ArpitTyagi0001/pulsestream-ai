package com.streamanalytics.query_service.controller;

import com.streamanalytics.query_service.entity.Event;
import com.streamanalytics.query_service.service.EventService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/all")
    public List<Event> getAllEvent(){
        return eventService.getAllEvent();
    }

    @GetMapping("/get/{EventId}")
    public Event getEventById(@PathVariable Long EventId){
        return eventService.EventById(EventId);
    }
}
