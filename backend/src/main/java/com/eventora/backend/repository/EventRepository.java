package com.eventora.backend.repository;
import com.eventora.backend.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> { }
