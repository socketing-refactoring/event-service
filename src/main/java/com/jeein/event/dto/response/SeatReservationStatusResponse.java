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
@AllArgsConstructor
@SuperBuilder
public class SeatReservationStatusResponse extends FlatSeatResponse {

    private String reservationId;
    private String reserverId;
    private String reserverEmail;

    public static SeatReservationStatusResponse convertToSeatReservationStatusFromEntity(
            Seat seat, ReservationResponse reservationResponse) {
        if (reservationResponse == null) {
            return SeatReservationStatusResponse.builder()
                    .id(seat.getId().toString())
                    .areaId(seat.getArea().getId().toString())
                    .cx(seat.getCx())
                    .cy(seat.getCy())
                    .row(seat.getRow())
                    .number(seat.getNumber())
                    .reservationId(null)
                    .reserverId(null)
                    .reserverEmail(null)
                    .build();
        } else {
            return SeatReservationStatusResponse.builder()
                    .id(seat.getId().toString())
                    .areaId(seat.getArea().getId().toString())
                    .cx(seat.getCx())
                    .cy(seat.getCy())
                    .row(seat.getRow())
                    .number(seat.getNumber())
                    .reservationId(reservationResponse.getId())
                    .reserverId(reservationResponse.getReserverId())
                    .reserverEmail(reservationResponse.getReserverEmail())
                    .build();
        }
    }
}
