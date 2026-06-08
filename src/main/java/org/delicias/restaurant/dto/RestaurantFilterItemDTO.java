package org.delicias.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import org.delicias.common.dto.restaurant.StoreType;

import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record RestaurantFilterItemDTO(
        Integer id,
        String name,
        String picture,
        String address,
        String storeType
) { }
