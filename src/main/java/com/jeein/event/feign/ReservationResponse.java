package com.jeein.event.feign;

import lombok.Getter;

@Getter
public class ReservationResponse {
    private String id;
    private String seatId;
    private String reserverId;
    private String reserverEmail;
}
