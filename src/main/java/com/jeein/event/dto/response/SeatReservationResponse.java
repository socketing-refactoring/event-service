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
public class SeatReservationResponse {
    private String id;
    private String areaId;
    private String reservationId;
    private String reserverId;
    private String reserverName;
    private String reserverEmail;

    public static SeatReservationResponse of(Seat seat, ReservationResponse reservation) {
        if (reservation == null) {
            return SeatReservationResponse.builder().id(seat.getId().toString())
                            .areaId(seat.getArea().getId().toString()).reservationId(null).reserverName(null)
                            .reserverEmail(null).build();
        } else {
            return SeatReservationResponse.builder().id(seat.getId().toString())
                            .areaId(seat.getArea().getId().toString())
                            .reservationId(reservation.getReserverId())
                            .reserverName(reservation.getReserverName())
                            .reserverEmail(reservation.getReserverEmail()).build();
        }
    }
}
