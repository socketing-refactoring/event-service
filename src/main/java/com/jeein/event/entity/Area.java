package com.jeein.event.entity;

import com.jeein.event.dto.request.AreaRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"event_id", "label"}))
public class Area extends BaseEntity {

    @Column(nullable = false, length = 10)
    private String label;

    @Min(0)
    @Max(Integer.MAX_VALUE)
    @Column(nullable = false)
    private int price;

    @Column private String areaMap;

    @ManyToOne private Event event;

    @OneToMany(
            mappedBy = "area",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Seat> seats;

    public static Area toEntity(AreaRequest area, Event event) {
        return Area.builder()
                .label(area.getLabel())
                .price(area.getPrice())
                .areaMap(area.getAreaMap())
                .event(event)
                .build();
    }

    public void addSeats(List<Seat> seats) {
        this.seats = seats;
    }
}
