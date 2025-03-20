package com.jeein.event.entity;

import com.jeein.event.dto.request.SeatRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Seat extends BaseEntity {

    @Min(0)
    @Max(Integer.MAX_VALUE)
    @Column(nullable = false)
    private int cx;

    @Min(0)
    @Max(Integer.MAX_VALUE)
    @Column(nullable = false)
    private int cy;

    @Min(0)
    @Max(Integer.MAX_VALUE)
    @Column(nullable = false)
    private int row;

    @Min(0)
    @Max(Integer.MAX_VALUE)
    @Column(nullable = false)
    private int number;

    @ManyToOne private Area area;

    public static Seat toEntity(SeatRequest seat, Area area) {
        return Seat.builder()
                .cx(seat.getCx())
                .cy(seat.getCy())
                .row(seat.getRow())
                .number(seat.getNumber())
                .area(area)
                .build();
    }
}
