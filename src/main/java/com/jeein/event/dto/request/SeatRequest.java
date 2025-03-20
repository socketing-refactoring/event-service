package com.jeein.event.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SeatRequest {

    @NotNull(message = "좌석의 x좌표를 입력해 주세요.")
    @Min(value = 0, message = "좌석의 x좌표는 0 이상이어야 합니다.")
    @Max(value = Integer.MAX_VALUE, message = "시스템에서 처리할 수 없는 숫자입니다.")
    private int cx;

    @NotNull(message = "좌석의 y좌표를 입력해 주세요.")
    @Min(value = 0, message = "좌석의 y좌표는 0 이상이어야 합니다.")
    @Max(value = Integer.MAX_VALUE, message = "시스템에서 처리할 수 없는 숫자입니다.")
    private int cy;

    @NotNull(message = "좌석 행 번호를 입력해 주세요.")
    @Min(value = 1, message = "좌석 행 번호는 1 이상이어야 합니다.")
    @Max(value = Integer.MAX_VALUE, message = "시스템에서 처리할 수 없는 숫자입니다.")
    private int row;

    @NotNull(message = "좌석 번호를 입력해 주세요.")
    @Min(value = 1, message = "좌석 번호는 1 이상이어야 합니다.")
    @Max(value = Integer.MAX_VALUE, message = "시스템에서 처리할 수 없는 숫자입니다.")
    private int number;
}
