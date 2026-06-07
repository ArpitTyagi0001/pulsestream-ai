package com.streamanalytics.event_consumer_service.repo;

import com.streamanalytics.event_consumer_service.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepo extends JpaRepository<Long , Event> {
}
