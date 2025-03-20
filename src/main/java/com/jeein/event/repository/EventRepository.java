package com.jeein.event.repository;

import com.jeein.event.entity.Event;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

    @Query("SELECT e FROM Event e WHERE e.deletedAt IS NULL")
    List<Event> findAll();

    @Query("SELECT e FROM Event e WHERE e.id = :eventId AND e.deletedAt IS NULL")
    Optional<Event> findById(@Param("eventId") UUID eventId);

    @Query("SELECT e FROM Event e WHERE e.title = :title AND e.deletedAt IS NULL")
    Optional<Event> findByTitle(String title);

    @Modifying // Query executed via 'getResultList()' or 'getSingleResult()' must be a 'select' query
    @Query("UPDATE Event e SET e.deletedAt = :deletedAt WHERE e.id = :eventId")
    void softDeleteEvent(@Param("eventId") UUID eventId, @Param("deletedAt") Instant deletedAt);
}
