package com.jeein.event.feign;

import com.jeein.event.dto.CommonResponse;
import com.jeein.event.dto.feign.JoinRequestDTO;
import com.jeein.event.dto.feign.JoinResponseDTO;
import com.jeein.event.dto.feign.ManagerResponseDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "member-service")
public interface MemberServiceFeignClient {

    @GetMapping("/api/v1/managers/{managerId}")
    public ResponseEntity<CommonResponse<ManagerResponseDTO>> getOneManager(@PathVariable String managerId);

    @PostMapping("/api/v1/managers/join")
    public ResponseEntity<CommonResponse<JoinResponseDTO>> joinManager(
                    @Valid @RequestBody JoinRequestDTO joinRequestDTO);

    @DeleteMapping("/{managerId}/hard")
    public ResponseEntity<CommonResponse<Void>> hardDeleteManager(@PathVariable String managerId);
}
