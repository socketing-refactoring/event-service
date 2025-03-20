package com.jeein.event.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EventRequest {
    @NotEmpty(message = "제목을 입력해 주세요.")
    @Size(min = 1, max = 20, message = "제목은 1자 이상 20자 이하로 입력해 주세요.")
    private String title;

    @NotEmpty(message = "설명을 입력해 주세요.")
    @Size(min = 1, max = 100, message = "설명은 1자 이상 100자 이하로 입력해 주세요.")
    private String description;

    @NotEmpty(message = "공연 장소를 입력해 주세요.")
    @Size(min = 1, max = 20, message = "공연 장소는는 1자 이상 20자 이하로 입력해 주세요.")
    private String place;

    @NotEmpty(message = "공연 아티스트를 입력해 주세요.")
    @Size(min = 1, max = 10, message = "공연 아티스트는 1자 이상 10자 이하로 입력해 주세요.")
    private String artist;

    @NotNull(message = "포스터를 입력해 주세요.")
    private MultipartFile thumbnail;

    @NotNull(message = "공연 오픈 일정을 입력해 주세요.")
    private Instant eventOpenTime;

    @NotNull(message = "티켓팅 오픈 일정을 입력해 주세요.")
    private Instant ticketingOpenTime;

    @NotEmpty(message = "좌석 배치도를 등록해 주세요.")
    private String totalMap;

    @NotNull(message = "공연 구역과 좌석 정보를 등록해 주세요.")
    private String areas;

    @NotNull(message = "공연 일정을 입력해 주세요.")
    private List<Instant> eventDatetimes;
}
