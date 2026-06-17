package com.streamanalytics.query_service.repo;

import com.streamanalytics.query_service.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepo extends JpaRepository<Event, Long> {

    List<Event> findTop10ByOrderByEventAtDesc();
}
