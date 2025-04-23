package com.jeein.event.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class AreaReservationStatistics {
    private String id;
    private String label;
    private int price;
    private int reservedCount;

    public static AreaReservationStatistics of(String id, String label, int price, int reservedCount) {
        return AreaReservationStatistics.builder().id(id).label(label).price(price)
                        .reservedCount(reservedCount).build();
    }
}
