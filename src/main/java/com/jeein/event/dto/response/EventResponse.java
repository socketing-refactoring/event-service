package com.jeein.event.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jeein.event.entity.Event;
import com.jeein.event.entity.EventDatetime;
import java.time.Instant;
import java.util.List;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EventResponse {
    private String id;
    private String title;
    private String description;
    private String place;
    private String artist;
    private List<EventDatetimeResponse> eventDatetimes;
    private Instant eventOpenTime;
    private Instant ticketingOpenTime;

    private String totalMap;
    private List<AreaResponse> areas;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class EventDatetimeResponse {
        private String id;
        private Instant datetime;

        private static EventDatetimeResponse convertToEventDatetimeResponseFromEntity(
                EventDatetime eventDatetime) {
            return new EventDatetimeResponse(
                    eventDatetime.getId().toString(), eventDatetime.getDatetime());
        }
    }

    public static EventResponse convertToPlainEventFromEntity(Event event) {
        return EventResponse.builder()
                .id(event.getId().toString())
                .title(event.getTitle())
                .description(event.getDescription())
                .place(event.getPlace())
                .artist(event.getArtist())
                .eventDatetimes(
                        event.getEventDatetimes().stream()
                                .map(
                                        EventDatetimeResponse
                                                ::convertToEventDatetimeResponseFromEntity)
                                .toList())
                .eventOpenTime(event.getEventOpenTime())
                .ticketingOpenTime(event.getTicketingOpenTime())
                .build();
    }

    public static EventResponse convertToDeatiledEvent(Event event) {
        return EventResponse.builder()
                .id(event.getId().toString())
                .title(event.getTitle())
                .description(event.getDescription())
                .place(event.getPlace())
                .artist(event.getArtist())
                .eventDatetimes(
                        event.getEventDatetimes().stream()
                                .map(
                                        EventDatetimeResponse
                                                ::convertToEventDatetimeResponseFromEntity)
                                .toList())
                .eventOpenTime(event.getEventOpenTime())
                .ticketingOpenTime(event.getTicketingOpenTime())
                .totalMap(event.getTotalMap())
                .areas(event.getAreas().stream().map(AreaResponse::fromEntity).toList())
                .build();
    }
}
