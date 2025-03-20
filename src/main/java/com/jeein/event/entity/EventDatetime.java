package com.jeein.event.entity;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"event_id", "datetime"}))
public class EventDatetime extends BaseEntity {
    @Column(nullable = false)
    private Instant datetime;

    @ManyToOne private Event event;

    public static EventDatetime toEntity(Instant datetime, Event event) {
        return new EventDatetime(datetime, event);
    }
}
