package org.delicias.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RestaurantFilterItemDTO(
        Integer id,
        String name,
        @JsonFormat(pattern="dd/MM/yyyy HH:mm:ss")
        LocalDateTime createdAt,
        @JsonFormat(pattern="dd/MM/yyyy HH:mm:ss")
        LocalDateTime updatedAt,
        String picture
) { }
