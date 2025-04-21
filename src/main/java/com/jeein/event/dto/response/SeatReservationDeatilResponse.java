package com.jeein.event.dto.response;

import com.jeein.event.entity.Seat;
import com.jeein.event.feign.ReservationResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder
public class SeatReservationDeatilResponse extends FlatSeatResponse {

    private String reservationId;
    private String reserverId;
    private String reserverName;
    private String reserverEmail;

    public static SeatReservationDeatilResponse of(
            Seat seat, ReservationResponse reservationResponse) {
        if (reservationResponse == null) {
            return SeatReservationDeatilResponse.builder()
                    .id(seat.getId().toString())
                    .areaId(seat.getArea().getId().toString())
                    .areaLabel(seat.getArea().getLabel())
                    .areaPrice(seat.getArea().getPrice())
                    .cx(seat.getCx())
                    .cy(seat.getCy())
                    .row(seat.getRow())
                    .number(seat.getNumber())
                    .reservationId(null)
                    .reserverId(null)
                    .reserverName(null)
                    .reserverEmail(null)
                    .build();
        } else {
            return SeatReservationDeatilResponse.builder()
                    .id(seat.getId().toString())
                    .areaId(seat.getArea().getId().toString())
                    .areaLabel(seat.getArea().getLabel())
                    .areaPrice(seat.getArea().getPrice())
                    .cx(seat.getCx())
                    .cy(seat.getCy())
                    .row(seat.getRow())
                    .number(seat.getNumber())
                    .reservationId(reservationResponse.getId())
                    .reserverId(reservationResponse.getReserverId())
                    .reserverName(reservationResponse.getReserverName())
                    .reserverEmail(reservationResponse.getReserverEmail())
                    .build();
        }
    }
}
