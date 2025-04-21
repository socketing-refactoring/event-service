package com.jeein.event.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeein.event.dto.CommonResponse;
import com.jeein.event.dto.request.AreaRequest;
import com.jeein.event.dto.request.EventRequest;
import com.jeein.event.dto.request.ParsedEventRequest;
import com.jeein.event.dto.response.*;
import com.jeein.event.exception.CustomValidationException;
import com.jeein.event.exception.ErrorCode;
import com.jeein.event.service.EventService;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final EventService eventService;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    /* 전체 공연 조회 (eventDatetimes 포함, totalMap 미포함) */
    @GetMapping
    public ResponseEntity<CommonResponse<List<EventResponse>>> getEventList(
            @RequestParam(required = false) String eventDatetimeId) {
        return ResponseEntity.ok(eventService.getEventList(eventDatetimeId));
    }

    /* 단일 공연 조회 (eventDatetimes 포함, totalMap 미포함) */
    @GetMapping("/{eventId}")
    public ResponseEntity<CommonResponse<EventResponse>> getEvent(@PathVariable String eventId) {
        return ResponseEntity.ok(eventService.getOneEvent(eventId));
    }

    /* 단일 공연 상세 조회 (eventDatetimes, totalMap, areas, seats 포함) */
    @GetMapping("/{eventId}/detail")
    public ResponseEntity<CommonResponse<EventResponse>> getOneEventDetails(
            @PathVariable String eventId) {
        return ResponseEntity.ok(eventService.getOneEventDetails(eventId));
    }

    /* 단일 공연 좌석 조회 (area 정보 미포함) */
    @GetMapping("/{eventId}/seats")
    public ResponseEntity<CommonResponse<List<SeatResponse>>> getEventSeats(
            @PathVariable String eventId) {
        return ResponseEntity.ok(eventService.getEventSeats(eventId));
    }

    /* 단일 공연 좌석 상세 조회 (area 정보 flat하게 반환) */
    @GetMapping("/{eventId}/seats/detail")
    public ResponseEntity<CommonResponse<List<FlatSeatResponse>>> getEventSeatDetails(
            @PathVariable String eventId) {
        return ResponseEntity.ok(eventService.getEventSeatDetails(eventId));
    }

    /* 단일 공연 일정별 좌석 예약 현황 조회 (area 정보 미포함) */
    @GetMapping("/{eventId}/dates/{eventDatetimeId}/seat-reservation")
    public ResponseEntity<CommonResponse<List<SeatReservationResponse>>>
    getEventSeatReservations(
            @PathVariable String eventId, @PathVariable String eventDatetimeId) {
        return ResponseEntity.ok(eventService.getEventSeatReservations(eventId, eventDatetimeId));
    }

    /* 단일 공연 일정별 좌석 예약 현황 상세 조회 (area 정보, reservation 정보 flat하게 포함) */
    @GetMapping("/{eventId}/dates/{eventDatetimeId}/seat-reservation/detail")
    public ResponseEntity<CommonResponse<List<SeatReservationDeatilResponse>>>
            getEventSeatReservationDetails(
                    @PathVariable String eventId, @PathVariable String eventDatetimeId) {
        return ResponseEntity.ok(eventService.getEventSeatReservationDetails(eventId, eventDatetimeId));
    }

    /* 단일 공연 수입 */
    @GetMapping("/{eventId}/dates/{eventDatetimeId}/statistics/revenue")
    public ResponseEntity<CommonResponse<Long>> getEventRevenue(
            @PathVariable String eventId, @PathVariable String eventDatetimeId) {
        return ResponseEntity.ok(eventService.getEventRevenue(eventId, eventDatetimeId));
    }

    /* 단일 공연 구역별 예매 현황 */
    @GetMapping("/{eventId}/dates/{eventDatetimeId}/statistics/area-reservation")
    public ResponseEntity<CommonResponse<List<AreaReservationStatistics>>>
            getAreaReservationStatistics(
                    @PathVariable String eventId, @PathVariable String eventDatetimeId) {
        return ResponseEntity.ok(
                eventService.getAreaReservationStatistics(eventId, eventDatetimeId));
    }

    /* 공연 정보 생성 (eventDatetimes, areas, seats 포함) */
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponse<EventResponse>> createEvent(@Valid EventRequest request)
            throws JsonProcessingException {
        List<AreaRequest> areaList =
                objectMapper.readValue(
                        request.getAreas(), new TypeReference<List<AreaRequest>>() {});

        BindException bindException = new BindException(areaList, "areaList");
        ValidationUtils.invokeValidator(validator, areaList, bindException);
        if (bindException.hasErrors()) {
            throw new CustomValidationException(
                    ErrorCode.INVALID_REQUEST_VALUE, bindException.getBindingResult());
        }

        List<Instant> eventDatetimes =
                objectMapper.readValue(
                        request.getEventDatetimes(), new TypeReference<List<Instant>>() {});

        ParsedEventRequest parsedEvent = ParsedEventRequest.parse(request);
        parsedEvent.addAreas(areaList);
        parsedEvent.addEventDatetimes(eventDatetimes);
        return ResponseEntity.ok(eventService.saveEvent(parsedEvent));
    }

    /* 단일 공연 삭제 */
    @DeleteMapping("/{eventId}")
    public ResponseEntity<CommonResponse<Object>> deleteEvent(@PathVariable String eventId) {
        return ResponseEntity.ok(eventService.softDeleteEvent(eventId));
    }
}
