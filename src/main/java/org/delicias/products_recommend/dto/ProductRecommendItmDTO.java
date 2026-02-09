package org.delicias.products_recommend.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductRecommendItmDTO(
        Integer id,
        Integer productTmplId,
        String name,
        String description,
        BigDecimal listPrice,
        String pictureUrl,
        Short sequence
) { }
