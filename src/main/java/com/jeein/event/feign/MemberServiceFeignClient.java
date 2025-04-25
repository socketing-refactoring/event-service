package com.jeein.event.feign;

import com.jeein.event.dto.CommonResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "member-service")
public interface MemberServiceFeignClient {

    @PostMapping("/api/v1/managers/join")
    public ResponseEntity<CommonResponse<JoinResponseDTO>> joinManager(
                    @Valid @RequestBody JoinRequestDTO joinRequestDTO);

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<CommonResponse<Void>> hardDeleteManager(@PathVariable String id);
}
