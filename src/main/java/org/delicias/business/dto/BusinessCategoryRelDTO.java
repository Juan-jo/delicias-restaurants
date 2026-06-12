package org.delicias.business.dto;

import org.delicias.restaurant.dto.RestaurantTmplOptionDTO;

public record BusinessCategoryRelDTO(
        Integer id,
        Short sequence,
        Boolean active,
        RestaurantTmplOptionDTO restaurant
) {

}
