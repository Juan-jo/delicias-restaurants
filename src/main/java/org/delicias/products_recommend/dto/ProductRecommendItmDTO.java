package org.delicias.products_recommend.dto;

import lombok.Builder;

@Builder
public record ProductRecommendItmDTO(
        Integer id,
        String productName,
        String productImageUrl,
        Short sequence
) { }
