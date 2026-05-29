package com.streamanalytics.event_service.controller;
import com.streamanalytics.event_service.service.KafkaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/location")
public class LocationController {

    KafkaService kafkaService;

    public LocationController(KafkaService kafkaService) {
        this.kafkaService = kafkaService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> updatedLocation(String location){
        kafkaService.updatedLocation("location updated");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
