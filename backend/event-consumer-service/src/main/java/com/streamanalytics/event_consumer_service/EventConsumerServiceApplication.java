package com.streamanalytics.event_consumer_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class EventConsumerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventConsumerServiceApplication.class, args);
	}

}
