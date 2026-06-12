package org.delicias.restaurant.dto;

public record RestaurantTmplOptionDTO(
        Integer id,
        String name,
        String storeTypeDesc,
        String address,
        String pictureUrl
) { }
