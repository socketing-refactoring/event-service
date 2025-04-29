package com.jeein.event.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jeein.event.dto.CommonResponse;
import com.jeein.event.dto.request.EventRequest;
import com.jeein.event.dto.response.AreaReservationStatistics;
import com.jeein.event.dto.response.EventResponse;
import com.jeein.event.dto.response.FlatSeatResponse;
import com.jeein.event.dto.response.SeatReservationDeatilResponse;
import com.jeein.event.dto.response.SeatReservationResponse;
import com.jeein.event.dto.response.SeatResponse;
import com.jeein.event.exception.CustomValidationException;
import com.jeein.event.exception.ErrorCode;
import com.jeein.event.exception.EventException;
import com.jeein.event.service.EventService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final EventService eventService;

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
    public ResponseEntity<CommonResponse<EventResponse>> getOneEventDetail(@PathVariable String eventId,
        @RequestParam(required = false, defaultValue = "false") boolean excludeSeat) {
        return ResponseEntity.ok(eventService.getOneEventDetails(eventId, excludeSeat));
    }

    /* 공연 정보 생성 (eventDatetimes, areas, seats 포함) */
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponse<EventResponse>> createEvent(
                    @RequestPart("request") @Valid EventRequest request,
                    @RequestPart("thumbnail") MultipartFile thumbnail, HttpServletRequest httpServletRequest)
                    throws JsonProcessingException {
        log.debug(request.toString());
        if (thumbnail.isEmpty()) {
            throw new EventException(ErrorCode.EMPTY_THUMBNAIL);
        }

        // Gateway Server에서 헤더에 추가한 회원 정보 추출
        httpServletRequest.getHeaderNames().asIterator().forEachRemaining(headerName -> {
            if (headerName.toLowerCase().startsWith("x-api-")) {
                log.info("Custom Header: {} = {}", headerName, httpServletRequest.getHeader(headerName));
            }
        });

        String managerId = Optional.ofNullable(httpServletRequest.getHeader("x-api-managerId"))
                        .filter(header -> !header.isEmpty())
                        .orElseThrow(() -> new CustomValidationException(ErrorCode.INVALID_TOKEN));

        CommonResponse<EventResponse> response = eventService.saveEvent(request, thumbnail, managerId);
        return ResponseEntity.status(HttpStatus.CREATED)
                        .location(URI.create("/api/v1/events/" + response.getData().getId())).body(response);
    }

    /* 단일 공연 삭제 */
    @DeleteMapping("/{eventId}")
    public ResponseEntity<CommonResponse<Object>> deleteEvent(@PathVariable String eventId) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(eventService.softDeleteEvent(eventId));
    }

    /* 단일 공연 하드 삭제 */
    @DeleteMapping("/{eventId}/hard")
    public ResponseEntity<CommonResponse<Object>> hardDeleteEvent(@PathVariable String eventId) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(eventService.hardDeleteEvent(eventId));
    }

    /*
     * 구체 정보 조회 API
     */

    /* 단일 공연 좌석 조회 (area 정보 미포함) */
    @GetMapping("/{eventId}/seats")
    public ResponseEntity<CommonResponse<List<SeatResponse>>> getEventSeats(@PathVariable String eventId) {
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
    public ResponseEntity<CommonResponse<List<SeatReservationResponse>>> getEventSeatReservations(
                    @PathVariable String eventId, @PathVariable String eventDatetimeId) {
        return ResponseEntity.ok(eventService.getEventSeatReservations(eventId, eventDatetimeId));
    }

    /* 단일 공연 일정별 좌석 예약 현황 상세 조회 (area 정보, reservation 정보 flat하게 포함) */
    @GetMapping("/{eventId}/dates/{eventDatetimeId}/seat-reservation/detail")
    public ResponseEntity<CommonResponse<List<SeatReservationDeatilResponse>>> getEventSeatReservationDetails(
                    @PathVariable String eventId, @PathVariable String eventDatetimeId) {
        return ResponseEntity.ok(eventService.getEventSeatReservationDetails(eventId, eventDatetimeId));
    }

    /* 단일 공연 수입 */
    @GetMapping("/{eventId}/dates/{eventDatetimeId}/statistics/revenue")
    public ResponseEntity<CommonResponse<Long>> getEventRevenue(@PathVariable String eventId,
                    @PathVariable String eventDatetimeId) {
        return ResponseEntity.ok(eventService.getEventRevenue(eventId, eventDatetimeId));
    }

    /* 단일 공연 구역별 예매 현황 */
    @GetMapping("/{eventId}/dates/{eventDatetimeId}/statistics/area-reservation")
    public ResponseEntity<CommonResponse<List<AreaReservationStatistics>>> getAreaReservationStatistics(
                    @PathVariable String eventId, @PathVariable String eventDatetimeId) {
        return ResponseEntity.ok(eventService.getAreaReservationStatistics(eventId, eventDatetimeId));
    }
}
