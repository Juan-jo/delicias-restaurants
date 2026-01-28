package org.delicias.scheduled.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.delicias.common.validation.OnUpdate;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Builder
public record RestaurantScheduleDTO(
        @NotNull(message = "The parameter is mandatory", groups = { OnUpdate.class})
        Integer id,

        @NotNull(message = "The parameter is mandatory", groups = { OnUpdate.class})
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime startTime,

        @NotNull(message = "The parameter is mandatory", groups = { OnUpdate.class})
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime endTime,

        @NotNull(message = "The parameter is mandatory", groups = { OnUpdate.class})
        DayOfWeek dayOfWeek,

        boolean available
) { }

