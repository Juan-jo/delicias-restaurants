package org.delicias.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.Builder;
import org.delicias.common.validation.OnUpdate;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record RestaurantAddressDTO(

        @NotNull(message = "ID is mandatory", groups = {OnUpdate.class})
        Integer id,

        @NotBlank(message = "Address is mandatory", groups = {OnUpdate.class})
        @Size(max = 150, message = "Address too long")
        String address,

        @NotNull(message = "Latitude is mandatory", groups = {OnUpdate.class})
        @DecimalMin(value = "-90.0", message = "Latitude must be greater than or equal to -90")
        @DecimalMax(value = "90.0", message = "Latitude must be less than or equal to 90")
        Double latitude,

        @NotNull(message = "Longitude is mandatory", groups = { OnUpdate.class})
        @DecimalMin(value = "-180.0", message = "Longitude must be greater than or equal to -180")
        @DecimalMax(value = "180.0", message = "Longitude must be less than or equal to 180")
        Double longitude

) { }
