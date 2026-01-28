package org.delicias.restaurant.dto;

import lombok.Getter;
import lombok.Setter;
import org.delicias.common.dto.BaseFilterDTO;

@Getter
@Setter
public class RestaurantFilterReqDTO extends BaseFilterDTO {
    private String name;
}
