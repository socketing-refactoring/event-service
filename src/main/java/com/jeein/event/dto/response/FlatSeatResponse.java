package com.jeein.event.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jeein.event.entity.Seat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public class FlatSeatResponse {
    private String id;
    private String areaId;
    private String areaLabel;
    private int areaPrice;
    private int cx;
    private int cy;
    private int row;
    private int number;

    public static FlatSeatResponse fromEntity(Seat seat) {
        return FlatSeatResponse.builder()
                .id(seat.getId().toString())
                .areaId(seat.getArea().getId().toString())
                .areaLabel(seat.getArea().getLabel())
                .areaPrice(seat.getArea().getPrice())
                .cx(seat.getCx())
                .cy(seat.getCy())
                .row(seat.getRow())
                .number(seat.getNumber())
                .build();
    }
}
