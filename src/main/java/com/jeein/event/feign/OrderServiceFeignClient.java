package com.jeein.event.feign;

import com.jeein.event.dto.CommonResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "order-service")
public interface OrderServiceFeignClient {

    @GetMapping("/api/v1/orders/reservations")
    ResponseEntity<CommonResponse<List<ReservationResponse>>> getReservationsByEvent(
            @RequestParam String eventId,
            @RequestParam String eventDatetimeId,
            @RequestParam(required = false, defaultValue = "false") boolean showCanceled);
}
