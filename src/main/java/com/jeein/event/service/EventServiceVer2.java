package com.jeein.event.service;

import com.jeein.event.dto.CommonResponse;
import com.jeein.event.dto.response.SeatAreaResponse;
import com.jeein.event.entity.Event;
import com.jeein.event.entity.Seat;
import com.jeein.event.exception.ErrorCode;
import com.jeein.event.exception.EventException;
import com.jeein.event.feign.OrderServiceFeignClient;
import com.jeein.event.repository.EventRepository;
import com.jeein.event.repository.SeatRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceVer2 {
    private final EventRepository eventRepository;
    private final SeatRepository seatRepository;
    private final OrderServiceFeignClient orderServiceFeignClient;

    @Transactional(readOnly = true)
    public CommonResponse<SeatAreaResponse> getOneEventSeat(String eventId, String seatId,
                    boolean includeArea) {
        Event event = eventRepository.findById(UUID.fromString(eventId))
                        .orElseThrow(() -> new EventException(ErrorCode.EVENT_NOT_FOUND));

        Seat seat = seatRepository.findById(UUID.fromString(seatId))
                        .orElseThrow(() -> new EventException(ErrorCode.SEAT_NOT_FOUND));

        if (!event.getId().equals(seat.getArea().getEvent().getId())) {
            throw new EventException(ErrorCode.EVENT_SEAT_MISMATCH);
        }

        SeatAreaResponse seatResponses = SeatAreaResponse.of(seat, includeArea);
        return CommonResponse.success("공연 좌석 조회 성공", "0", seatResponses);
    }

    @Transactional(readOnly = true)
    public CommonResponse<List<SeatAreaResponse>> getEventSeats(String eventId, List<String> seatIds,
                    boolean includeArea) {
        Event event = eventRepository.findById(UUID.fromString(eventId))
                        .orElseThrow(() -> new EventException(ErrorCode.EVENT_NOT_FOUND));

        List<Seat> seats;
        if (!seatIds.isEmpty()) {
            seats = seatRepository.findByIds(seatIds.stream().map(UUID::fromString).toList());
            if (seats.isEmpty()) {
                throw new EventException(ErrorCode.SEAT_NOT_FOUND);
            }

            if (!event.getId().equals(seats.getFirst().getArea().getEvent().getId())) {
                throw new EventException(ErrorCode.EVENT_SEAT_MISMATCH);
            }

        } else {
            seats = seatRepository.findByEventId(UUID.fromString(eventId));
        }

        List<SeatAreaResponse> seatResponses =
                        seats.stream().map(seat -> SeatAreaResponse.of(seat, includeArea)).toList();
        return CommonResponse.success("공연 좌석 목록 조회 성공", "0", seatResponses);
    }
}
