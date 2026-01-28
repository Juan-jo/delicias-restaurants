package org.delicias.scheduled.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.delicias.restaurant.domain.model.RestaurantTemplate;

import java.time.DayOfWeek;
import java.time.LocalTime;


@Entity
@Table(name = "restaurant_schedule")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantScheduled {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "restaurant_schedule_id_seq")
    @SequenceGenerator(
            name = "restaurant_schedule_id_seq",
            allocationSize = 1
    )
    private Integer id;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week")
    private DayOfWeek dayOfWeek;

    @ManyToOne
    @JoinColumn(name="restaurant_tmpl_id", referencedColumnName = "id")
    private RestaurantTemplate restaurantTmpl;

    private Boolean available;
}

