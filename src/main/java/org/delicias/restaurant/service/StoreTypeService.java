package org.delicias.restaurant.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.delicias.common.dto.restaurant.StoreType;
import org.delicias.restaurant.dto.StoreTypeOptionDTO;

import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class StoreTypeService {

    public List<StoreTypeOptionDTO> getStoreTypesList() {
        return Arrays.stream(StoreType.values())
                .map(type -> new StoreTypeOptionDTO(type, type.getDescription()))
                .toList();
    }
}
