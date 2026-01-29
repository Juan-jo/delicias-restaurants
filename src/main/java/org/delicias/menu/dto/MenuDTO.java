package org.delicias.menu.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.delicias.common.validation.OnCreate;
import org.delicias.common.validation.OnUpdate;

@Builder
public record MenuDTO(
        @NotNull(message = "The parameter is mandatory", groups = { OnUpdate.class})
        Integer id,

        @NotNull(message = "The parameter is mandatory", groups = {OnCreate.class, OnUpdate.class})
        String name,

        @NotNull(message = "The parameter is mandatory", groups = {OnCreate.class, OnUpdate.class})
        Short sequence,

        boolean available
) { }
