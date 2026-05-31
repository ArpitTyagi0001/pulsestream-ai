package com.streamanalytics.event_service.controller;
import com.streamanalytics.event_service.service.KafkaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/location")
public class LocationController {

    private final KafkaService kafkaService;

    public LocationController(KafkaService kafkaService) {
        this.kafkaService = kafkaService;
    }

    @PostMapping("/update")
    public ResponseEntity<String> updatedLocation(@RequestParam String location){
        kafkaService.updatedLocation(location);
        return ResponseEntity.accepted().body("Location Updated");
    }
}
