package com.jeein.event.service;

import com.jeein.event.dto.CommonResponse;
import com.jeein.event.dto.request.ParsedEventRequest;
import com.jeein.event.dto.response.EventResponse;
import com.jeein.event.dto.response.FlatSeatResponse;
import com.jeein.event.dto.response.SeatReservationStatusResponse;
import com.jeein.event.entity.Area;
import com.jeein.event.entity.Event;
import com.jeein.event.entity.EventDatetime;
import com.jeein.event.entity.Seat;
import com.jeein.event.exception.ErrorCode;
import com.jeein.event.exception.EventException;
import com.jeein.event.exception.OrderServiceFeignClientException;
import com.jeein.event.exception.UploadException;
import com.jeein.event.feign.OrderServiceFeignClient;
import com.jeein.event.feign.ReservationResponse;
import com.jeein.event.repository.EventRepository;
import com.jeein.event.util.UploadManager;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartException;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final OrderServiceFeignClient orderServiceFeignClient;
    private final UploadManager uploadManager;

    @Transactional(readOnly = true)
    public CommonResponse<List<EventResponse>> getEventList() {
        List<EventResponse> response =
                eventRepository.findAll().stream()
                        .map(EventResponse::convertToPlainEventFromEntity)
                        .toList();
        return CommonResponse.success("전체 공연 목록 조회 성공", "0", response);
    }

    @Transactional(readOnly = true)
    public CommonResponse<EventResponse> getOneEvent(String eventId) {
        Event event =
                eventRepository
                        .findById(UUID.fromString(eventId))
                        .orElseThrow(() -> new EventException(ErrorCode.EVENT_NOT_FOUND));
        EventResponse eventResponse = EventResponse.convertToPlainEventFromEntity(event);

        return CommonResponse.success("단일 공연 조회 성공", "0", eventResponse);
    }

    @Transactional(readOnly = true)
    public CommonResponse<EventResponse> getOneEventDetails(String eventId) {
        Event event =
                eventRepository
                        .findById(UUID.fromString(eventId))
                        .orElseThrow(() -> new EventException(ErrorCode.EVENT_NOT_FOUND));
        EventResponse eventResponse = EventResponse.convertToDeatiledEvent(event);

        return CommonResponse.success("단일 공연 상세 조회 성공", "0", eventResponse);
    }

    @Transactional(readOnly = true)
    public CommonResponse<List<FlatSeatResponse>> getEventSeatDetails(String eventId) {
        Event event =
                eventRepository
                        .findById(UUID.fromString(eventId))
                        .orElseThrow(() -> new EventException(ErrorCode.EVENT_NOT_FOUND));

        List<FlatSeatResponse> seatResponse =
                event.getAreas().stream()
                        .flatMap(area -> area.getSeats().stream().map(FlatSeatResponse::fromEntity))
                        .toList();
        return CommonResponse.success("공연 좌석 상세 조회 성공", "0", seatResponse);
    }

    public CommonResponse<List<SeatReservationStatusResponse>> getEventSeatReservations(
            String eventId, String eventDatetimeId) {

        Event event =
                eventRepository
                        .findById(UUID.fromString(eventId))
                        .orElseThrow(() -> new EventException(ErrorCode.EVENT_NOT_FOUND));
        List<Seat> seats =
                event.getAreas().stream().flatMap(area -> area.getSeats().stream()).toList();

        ResponseEntity<CommonResponse<List<ReservationResponse>>> orderServiceResponse =
                orderServiceFeignClient.getReservationsByEvent(eventId, eventDatetimeId, false);
        if (orderServiceResponse.getStatusCode().isError()) {
            log.error("Error message: {}", orderServiceResponse.getBody().getMessage());
            log.error("Error details: {}", orderServiceResponse.getBody().getErrors());
            throw new OrderServiceFeignClientException(ErrorCode.FEIGN_CONNECTION_ERROR);
        }

        Map<String, ReservationResponse> reservationMap =
                orderServiceResponse.getBody().getData().stream()
                        .collect(
                                Collectors.toMap(
                                        ReservationResponse::getSeatId,
                                        reservationResponse -> reservationResponse));

        List<SeatReservationStatusResponse> seatReservationStatusResponses =
                seats.stream()
                        .map(
                                seat -> {
                                    ReservationResponse reservationResponse =
                                            reservationMap.get(seat.getId().toString());
                                    return SeatReservationStatusResponse
                                            .convertToSeatReservationStatusFromEntity(
                                                    seat, reservationResponse);
                                })
                        .collect(Collectors.toList());

        return CommonResponse.success("좌석 예약 현황 조회 성공", "0", seatReservationStatusResponses);
    }

    @Transactional
    public CommonResponse<EventResponse> saveEvent(ParsedEventRequest eventRequest) {
        Optional<Event> existingEvent = eventRepository.findByTitle(eventRequest.getTitle());
        if (existingEvent.isPresent()) {
            throw new EventException(ErrorCode.EVENT_ALREADY_EXISTS);
        }

        // 포스터 업로드
        String newFileName;
        try {
            newFileName =
                    uploadManager.uploadFile(eventRequest.getThumbnail(), eventRequest.getTitle());
        } catch (MultipartException e) {
            throw new EventException(ErrorCode.INVALID_MULTIPARTFILE);
        } catch (IOException e) {
            throw new UploadException(ErrorCode.UPLOAD_ERROR);
        }

        // 공연 저장
        Event event = Event.toEntity(eventRequest, newFileName);
        event.addEventDatetimes(eventRequest.getEventDatetimes().stream().map(datetime -> EventDatetime.toEntity(datetime, event)).toList());

        List<Area> areas =
                eventRequest.getAreas().stream()
                        .map(
                                area -> {
                                    Area areaEntity = Area.toEntity(area, event);
                                    List<Seat> seatEntityList =
                                            area.getSeats().stream()
                                                    .map(seat -> Seat.toEntity(seat, areaEntity))
                                                    .toList();
                                    areaEntity.addSeats(seatEntityList);
                                    return areaEntity;
                                })
                        .toList();
        event.addAreas(areas);

        Event savedEvent = eventRepository.save(event);
        EventResponse response = EventResponse.convertToDeatiledEvent(savedEvent);
        log.debug(response.getArtist());

        return CommonResponse.success("공연 등록 성공", "0", response);
    }

    @Transactional
    public CommonResponse<Object> softDeleteEvent(String eventId) {
        Event event =
                eventRepository
                        .findById(UUID.fromString(eventId))
                        .orElseThrow(() -> new EventException(ErrorCode.EVENT_NOT_FOUND));

        if (event.getDeletedAt() != null) {
            throw new EventException(ErrorCode.EVENT_ALREADY_DELETED);
        }

        eventRepository.softDeleteEvent(UUID.fromString(eventId), Instant.now());
        return CommonResponse.success("공연 삭제 성공", "0", null);
    }
}
