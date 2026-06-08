package org.delicias.restaurant.dto;

import org.delicias.common.dto.restaurant.StoreType;

public record StoreTypeOptionDTO(
        StoreType typeId, String name
) {
}
