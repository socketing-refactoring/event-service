package com.jeein.event.dto.feign;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JoinResponseDTO {
    private String id;
    private String name;
    private String nickname;
    private String email;
}
