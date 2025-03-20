package com.jeein.event.entity;

import com.jeein.event.dto.request.ParsedEventRequest;
import jakarta.persistence.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.time.Instant;
import java.util.List;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
public class Event extends DeletableEntity {

    @Column(nullable = false, length = 20)
    String title;

    @Column(nullable = false, length = 100)
    String description;

    @Column(nullable = false)
    private String thumbnail;

    @Column(nullable = false)
    private String totalMap;

    @Column(nullable = false, length = 20)
    private String place;

    @Column(nullable = false, length = 10)
    private String artist;

    @Column(nullable = false)
    private Instant eventOpenTime;

    @Column(nullable = false)
    private Instant ticketingOpenTime;

    @OneToMany(
            mappedBy = "event",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<EventDatetime> eventDatetimes;

    @OneToMany(
            mappedBy = "event",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Area> areas;

    public static Event toEntity(ParsedEventRequest request, String thumbnailPath) {
        return Event.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .place(request.getPlace())
                .artist(request.getArtist())
                .thumbnail(thumbnailPath)
                .eventOpenTime(request.getEventOpenTime())
                .ticketingOpenTime(request.getTicketingOpenTime())
                .totalMap(request.getTotalMap())
                .build();
    }

    public void addEventDatetimes(List<EventDatetime> eventDatetimes) {
        this.eventDatetimes = eventDatetimes;
    }

    public void addAreas(List<Area> areas) {
        this.areas = areas;
    }
}
