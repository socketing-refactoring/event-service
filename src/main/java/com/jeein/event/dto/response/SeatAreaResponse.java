package com.jeein.event.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jeein.event.entity.Seat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SeatAreaResponse {
    private String id;
    private int cx;
    private int cy;
    private int row;
    private int number;

    private Area area;

    @AllArgsConstructor
    private static class Area {
        private String id;
        private String label;
        private int price;
        private String eventId;
    }

    public static SeatAreaResponse of(Seat seat, boolean includeArea) {
        SeatAreaResponseBuilder seatAreaResponseBuilder =
                        SeatAreaResponse.builder().id(seat.getId().toString()).cx(seat.getCx())
                                        .cy(seat.getCy()).row(seat.getRow()).number(seat.getRow());

        if (includeArea) {
            return seatAreaResponseBuilder.area(new Area(seat.getArea().getId().toString(),
                            seat.getArea().getLabel(), seat.getArea().getPrice(),
                            seat.getArea().getEvent().getId().toString())).build();
        }
        return seatAreaResponseBuilder.area(null).build();
    }
}
