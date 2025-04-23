package com.jeein.event.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString(exclude = {"areas", "eventDatetimes"})
public class EventRequest {
    @NotEmpty(message = "제목을 입력해 주세요.")
    @Size(max = 20, message = "제목은 20자 이하로 입력해 주세요.")
    private String title;

    @NotEmpty(message = "설명을 입력해 주세요.")
    @Size(max = 100, message = "설명은 100자 이하로 입력해 주세요.")
    private String description;

    @NotEmpty(message = "공연 장소를 입력해 주세요.")
    @Size(max = 20, message = "공연 장소는 20자 이하로 입력해 주세요.")
    private String place;

    @NotEmpty(message = "공연 아티스트를 입력해 주세요.")
    @Size(max = 10, message = "공연 아티스트는 10자 이하로 입력해 주세요.")
    private String artist;

    @NotNull(message = "공연 오픈 일정을 입력해 주세요.")
    private Instant eventOpenTime;

    @NotNull(message = "티켓팅 오픈 일정을 입력해 주세요.")
    private Instant ticketingOpenTime;

    @NotEmpty(message = "좌석 배치도를 등록해 주세요.")
    private String totalMap;

    @NotNull(message = "공연 구역과 좌석 정보를 등록해 주세요.")
    private List<AreaRequest> areas;

    @NotNull(message = "공연 일정을 입력해 주세요.")
    private List<Instant> eventDatetimes;

    public static EventRequest of(String title, String description, String place, String artist,
                    Instant eventOpenTime, Instant ticketingOpenTime, String totalMap,
                    List<AreaRequest> areas, List<Instant> eventDatetimes) {
        return EventRequest.builder().title(title).description(description).place(place).artist(artist)
                        .eventOpenTime(eventOpenTime).ticketingOpenTime(ticketingOpenTime).totalMap(totalMap)
                        .areas(areas).eventDatetimes(eventDatetimes).build();
    }
}
