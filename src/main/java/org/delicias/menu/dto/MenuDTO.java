package org.delicias.menu.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.delicias.common.validation.OnCreate;
import org.delicias.common.validation.OnUpdate;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record MenuDTO(
        @NotNull(message = "The parameter is mandatory", groups = { OnUpdate.class})
        Integer id,

        @NotNull(message = "The parameter is mandatory", groups = {OnCreate.class, OnUpdate.class})
        String name,

        @NotNull(message = "The parameter is mandatory", groups = {OnCreate.class, OnUpdate.class})
        Short sequence,

        boolean available,

        List<ProductDTO> products
) {



        @Builder
        public record ProductDTO(
                Integer id,
                String name,
                String description,
                String pictureUrl,
                BigDecimal listPrice
        ) {}
}
