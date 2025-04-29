package com.jeein.event.dto.feign;

import lombok.Getter;

@Getter
public class ReservationResponse {
    private String id;
    private String seatId;
    private String reserverId;
    private String reserverName;
    private String reserverEmail;
}
