package com.jeein.event.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.jeein.event.entity.Area;
import java.util.List;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
public class AreaResponse {
    private String id;
    private String eventId;
    private String label;
    private int price;
    private String areaMap;

    private List<SeatResponse> seats;

    public static AreaResponse toResponseWithoutSeats(Area area) {
        return AreaResponse.builder().id(area.getId().toString()).eventId(area.getEvent().getId().toString())
            .label(area.getLabel()).price(area.getPrice()).areaMap(area.getAreaMap())
            .build();
    }
    public static AreaResponse fromEntity(Area area) {
        return AreaResponse.builder().id(area.getId().toString()).eventId(area.getEvent().getId().toString())
                        .label(area.getLabel()).price(area.getPrice()).areaMap(area.getAreaMap())
                        .seats(area.getSeats().stream().map(SeatResponse::fromEntity).toList()).build();
    }
}
