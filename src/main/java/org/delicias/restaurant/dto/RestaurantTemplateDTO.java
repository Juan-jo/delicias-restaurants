package org.delicias.restaurant.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.delicias.common.dto.restaurant.StoreType;
import org.delicias.common.validation.OnCreate;
import org.delicias.common.validation.OnUpdate;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record RestaurantTemplateDTO(
        Integer id,
        String name,
        String description,
        String phone,
        String logoPicture,
        String coverPicture,
        String address,
        StoreType storeType
) { }
