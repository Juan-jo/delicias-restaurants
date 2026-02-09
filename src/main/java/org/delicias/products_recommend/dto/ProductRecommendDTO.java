package org.delicias.products_recommend.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductRecommendDTO(
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
    ) {}
}
