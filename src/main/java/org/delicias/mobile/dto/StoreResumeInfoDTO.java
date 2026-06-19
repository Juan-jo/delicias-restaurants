package org.delicias.mobile.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public record StoreResumeInfoDTO(
        String logoUrl,
        String coverUrl,
        String name,
        String address,
        Schedule schedule
) {

    public record Schedule(
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm a")
            LocalTime hourStart,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm a")
            LocalTime hourEnd,

            boolean available
    ) {

    }
}
