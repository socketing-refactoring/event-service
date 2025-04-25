package com.jeein.event.repository;

import com.jeein.event.entity.Seat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat, UUID> {

    @Query("SELECT s FROM Seat s " + "JOIN s.area a " + "JOIN a.event e "
                    + "WHERE s.id = :seatId AND e.deletedAt IS NULL")
    Optional<Seat> findById(@Param("seatId") UUID seatId);

    @Query("SELECT s FROM Seat s " + "JOIN s.area a " + "JOIN a.event e "
                    + "WHERE s.id IN :seatIds AND e.deletedAt IS NULL")
    List<Seat> findByIds(@Param("seatIds") List<UUID> seatIds);

    @Query("SELECT s FROM Seat s " + "JOIN s.area a " + "JOIN a.event e "
                    + "WHERE e.id = :eventId AND e.deletedAt IS NULL")
    List<Seat> findByEventId(@Param("eventId") UUID eventId);
}
