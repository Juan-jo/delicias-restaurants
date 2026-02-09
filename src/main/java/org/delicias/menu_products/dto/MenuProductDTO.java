package org.delicias.menu_products.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record MenuProductDTO(
        Integer id,
        Short sequence,
        Product product
) {

    @Builder
    public record Product(
            String name,
            String description,
            String picture,
            BigDecimal listPrice
    ) {
    }
}
