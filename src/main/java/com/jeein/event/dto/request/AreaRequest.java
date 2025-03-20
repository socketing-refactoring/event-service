package com.jeein.event.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AreaRequest {
    @NotEmpty(message = "구역 이름을 입력해 주세요.")
    private String label;

    @NotEmpty(message = "구역 가격을 입력해 주세요.")
    @Min(value = 0, message = "가격은 0원 이상 입력해 주세요.")
    @Max(value = Integer.MAX_VALUE, message = "시스템에서 처리할 수 없는 가격입니다.")
    private int price;

    @NotEmpty(message = "구역 맵을 등록해 주세요.")
    private String areaMap;

    @NotNull(message = "좌석 정보를 등록해 주세요.")
    private List<SeatRequest> seats;
}
