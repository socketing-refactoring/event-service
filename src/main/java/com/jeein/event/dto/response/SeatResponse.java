package com.jeein.event.dto.response;

import com.jeein.event.entity.Seat;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SeatResponse {
    private String id;
    private String areaId;
    private int cx;
    private int cy;
    private int row;
    private int number;

    public static SeatResponse fromEntity(Seat seat) {
        return SeatResponse.builder()
                .id(seat.getId().toString())
                .areaId(seat.getArea().getId().toString())
                .cx(seat.getCx())
                .cy(seat.getCy())
                .row(seat.getRow())
                .number(seat.getNumber())
                .build();
    }
}
