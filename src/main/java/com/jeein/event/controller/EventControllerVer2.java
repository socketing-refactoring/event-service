package com.jeein.event.controller;

import com.jeein.event.dto.CommonResponse;
import com.jeein.event.dto.response.SeatAreaResponse;
import com.jeein.event.service.EventServiceVer2;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/events")
@RequiredArgsConstructor
@Slf4j
public class EventControllerVer2 {

    private final EventServiceVer2 eventService;

    /*
     * 구체 정보 조회 API
     */

    /* 단일 공연 좌석 조회 */
    @GetMapping("/{eventId}/seats/{seatId}")
    public ResponseEntity<CommonResponse<SeatAreaResponse>> getOneEventSeat(@PathVariable String eventId,
                    @PathVariable String seatId,
                    @RequestParam(required = false, defaultValue = "true") boolean includeArea) {
        return ResponseEntity.ok(eventService.getOneEventSeat(eventId, seatId, includeArea));
    }

    @GetMapping("/{eventId}/seats")
    public ResponseEntity<CommonResponse<List<SeatAreaResponse>>> getEventSeats(@PathVariable String eventId,
                    @RequestParam(required = false) List<String> seatIds,
                    @RequestParam(required = false, defaultValue = "true") boolean includeArea) {
        if (seatIds == null) {
            seatIds = Collections.emptyList();
        }

        return ResponseEntity.ok(eventService.getEventSeats(eventId, seatIds, includeArea));
    }
}
