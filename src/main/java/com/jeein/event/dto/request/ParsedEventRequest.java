package com.jeein.event.dto.request;

import java.time.Instant;
import java.util.List;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ParsedEventRequest {
    private String title;
    private String description;
    private String place;
    private String artist;
    private MultipartFile thumbnail;
    private List<Instant> eventDatetimes;
    private Instant eventOpenTime;
    private Instant ticketingOpenTime;
    private String totalMap;
    private List<AreaRequest> areas;

    public static ParsedEventRequest parse(EventRequest request) {
        return ParsedEventRequest.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .place(request.getPlace())
                .artist(request.getArtist())
                .thumbnail(request.getThumbnail())
                .eventOpenTime(request.getEventOpenTime())
                .ticketingOpenTime(request.getTicketingOpenTime())
                .totalMap(request.getTotalMap())
                .eventDatetimes(request.getEventDatetimes())
                .build();
    }

    public void addAreas(List<AreaRequest> areas) {
        this.areas = areas;
    }
}
