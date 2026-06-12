package org.delicias.business.dto;

import jakarta.validation.constraints.NotNull;
import org.delicias.common.validation.OnCreate;
import org.delicias.common.validation.OnUpdate;

public record BusinessCategoryReqDTO(
        @NotNull(message = "The parameter is mandatory", groups = { OnUpdate.class})
        Integer id,

        @NotNull(message = "The parameter is mandatory", groups = {OnCreate.class})
        Integer zoneBusinessCategoryId,

        @NotNull(message = "The parameter is mandatory", groups = {OnCreate.class})
        Integer restaurantId,

        @NotNull(message = "The parameter is mandatory", groups = {OnCreate.class, OnUpdate.class})
        Short sequence,

        @NotNull(message = "The parameter is mandatory", groups = {OnCreate.class, OnUpdate.class})
        Boolean active
) { }
