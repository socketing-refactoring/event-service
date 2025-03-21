package com.jeein.event.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeein.event.dto.CommonResponse;
import com.jeein.event.dto.request.AreaRequest;
import com.jeein.event.dto.request.EventRequest;
import com.jeein.event.dto.request.ParsedEventRequest;
import com.jeein.event.dto.response.EventResponse;
import com.jeein.event.dto.response.FlatSeatResponse;
import com.jeein.event.dto.response.SeatReservationStatusResponse;
import com.jeein.event.exception.CustomValidationException;
import com.jeein.event.exception.ErrorCode;
import com.jeein.event.service.EventService;
import jakarta.validation.Valid;
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
    public ResponseEntity<CommonResponse<List<EventResponse>>> getEventList() {
        return ResponseEntity.ok(eventService.getEventList());
    }

    /* 단일 공연 조회 (eventDatetimes 포함, totalMap 미포함) */
    @GetMapping("/{eventId}")
    public ResponseEntity<CommonResponse<EventResponse>> getEvent(@PathVariable String eventId) {
        return ResponseEntity.ok(eventService.getOneEvent(eventId));
    }

    /* 단일 공연 상세 조회 (eventDatetimes, totalMap, areas, seats 포함) */
    @GetMapping("/{eventId}/details")
    public ResponseEntity<CommonResponse<EventResponse>> getOneEventDetails(
            @PathVariable String eventId) {
        return ResponseEntity.ok(eventService.getOneEventDetails(eventId));
    }

    /* 단일 공연 좌석 상세 조회 (area 정보 flat하게 반환) */
    @GetMapping("/{eventId}/seats/details")
    public ResponseEntity<CommonResponse<List<FlatSeatResponse>>> getEventSeatDetails(
            @PathVariable String eventId) {
        return ResponseEntity.ok(eventService.getEventSeatDetails(eventId));
    }

    /* 단일 공연 일정별 좌석 예약 현황 상세 조회 (area 정보, order/reservation 정보 flat하게 포함) */
    @GetMapping("/{eventId}/dates/{eventDatetimeId}/seats/reservation-status")
    public ResponseEntity<CommonResponse<List<SeatReservationStatusResponse>>> getEventSeatDetails(
            @PathVariable String eventId, @PathVariable String eventDatetimeId) {
        return ResponseEntity.ok(eventService.getEventSeatReservations(eventId, eventDatetimeId));
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

        ParsedEventRequest parsedEvent = ParsedEventRequest.parse(request);
        parsedEvent.addAreas(areaList);
        return ResponseEntity.ok(eventService.saveEvent(parsedEvent));
    }

    /* 단일 공연 삭제 */
    @DeleteMapping("/{eventId}")
    public ResponseEntity<CommonResponse<Object>> deleteEvent(@PathVariable String eventId) {
        return ResponseEntity.ok(eventService.softDeleteEvent(eventId));
    }
}
