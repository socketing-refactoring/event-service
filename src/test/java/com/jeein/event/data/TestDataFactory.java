package com.jeein.event.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jeein.event.dto.request.AreaRequest;
import com.jeein.event.dto.request.EventRequest;
import com.jeein.event.dto.request.SeatRequest;
import com.jeein.event.entity.Area;
import com.jeein.event.entity.Event;
import com.jeein.event.entity.EventDatetime;
import com.jeein.event.entity.Seat;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

public class TestDataFactory {

    public static Event createEvent(EventRequest eventRequest, String newFileName, String managerId) {
        Event event = Event.toEntity(eventRequest, newFileName, managerId);

        event.addEventDatetimes(
            eventRequest.getEventDatetimes().stream()
                .map(datetime -> EventDatetime.toEntity(datetime, event))
                .toList()
        );

        List<Area> areas = eventRequest.getAreas().stream()
            .map(area -> {
                Area areaEntity = Area.toEntity(area, event);
                List<Seat> seatEntityList = area.getSeats().stream()
                    .map(seat -> Seat.toEntity(seat, areaEntity))
                    .toList();
                areaEntity.addSeats(seatEntityList);
                return areaEntity;
            })
            .toList();
        event.addAreas(areas);

        return event;
    }
    public static List<EventRequest> createEventRequestList() {
        List<AreaRequest> mockAreas = createMockAreas();

        return IntStream.range(0, 2)
                        .mapToObj(i -> EventRequest.of("공연 제목 " + (i + 1), "공연 설명", "공연 장소", "공연 아티스트",
                                        Instant.parse("2025-04-25T05:00:00.000Z"),
                                        Instant.parse("2025-04-30T05:00:00.000Z"), "<svg>...</svg>",
                                        mockAreas,
                                        List.of(Instant.parse("2025-05-01T05:00:00.000Z"),
                                                Instant.parse("2025-05-02T05:00:00.000Z")
                                        )
                            )
                        )
                        .toList();
    }

    public static EventRequest createEventRequest() {
        List<AreaRequest> mockAreas = createMockAreas();

        return EventRequest.of("공연 제목", "공연 설명", "공연 장소", "공연 아티스트",
                        Instant.parse("2025-04-25T05:00:00.000Z"), Instant.parse("2025-04-30T05:00:00.000Z"),
                        "<svg>...</svg>", mockAreas, List.of(Instant.parse("2025-05-01T05:00:00.000Z"),
                                        Instant.parse("2025-05-02T05:00:00.000Z")));
    }

    public static List<AreaRequest> createMockAreas() {
        return IntStream.range(0, 2).mapToObj(i -> AreaRequest.of(String.valueOf((char) ('A' + i)), 50000,
                        "<svg>...</svg>",
                        IntStream.range(0, 2).mapToObj(
                                        j -> SeatRequest.of(100 * i + j, 200 * i + j, j / 10 + 1, j % 10 + 1))
                                        .toList()))
                        .toList();
    }

    public static MockMultipartFile createMockMultipartFile(EventRequest eventRequest)
                    throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return new MockMultipartFile("request", "", MediaType.APPLICATION_JSON_VALUE,
                        objectMapper.writeValueAsBytes(eventRequest));
    }

    public static MockMultipartFile createThumbnailMockMultipartFile() throws IOException {
        ClassPathResource file = new ClassPathResource("sample-thumbnail.jpg");
        return new MockMultipartFile("thumbnail", file.getFilename(), MediaType.IMAGE_JPEG_VALUE,
                        file.getInputStream());
    }
}
