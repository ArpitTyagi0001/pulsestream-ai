package com.streamanalytics.query_service.service;

import com.streamanalytics.query_service.entity.Event;
import com.streamanalytics.query_service.repo.EventRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EventService {
    private EventRepo eventRepo;

    public EventService(EventRepo eventRepo) {
        this.eventRepo = eventRepo;
    }


    @Cacheable(value = "all-events")
    public List<Event> getEvent() {
        return eventRepo.findAll();
    }

    @Cacheable(value = "event-by-id" , key = "#eventId")
    public Event getEventId(Long eventId) {
        return eventRepo.findById(eventId).orElseThrow(() -> new EntityNotFoundException("Event Not Found"));
    }

    @Cacheable(value = "total-events")
    public long count() {
        return eventRepo.count();
    }

    @Cacheable(value = "latest-events")
    public List<Event> getLatest() {
        return eventRepo.findTop10ByOrderByEventAtDesc();
    }

    @Cacheable(value = "location-count")
    public Map<String , Long> countLocation() {
       List<Event> events = eventRepo.findAll();

       return events.stream()
               .collect(Collectors.groupingBy(
                      Event::getLocation,
                      Collectors.counting()
               ));
    }
}