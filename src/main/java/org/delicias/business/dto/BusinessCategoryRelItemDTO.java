package org.delicias.business.dto;

public record BusinessCategoryRelItemDTO(
        Integer id,
        Short sequence,
        Boolean active,
        String restaurantName,
        String pictureUrl,
        String address
)
{ }
