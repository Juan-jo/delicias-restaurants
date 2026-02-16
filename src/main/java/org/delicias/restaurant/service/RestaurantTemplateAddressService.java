package org.delicias.restaurant.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.delicias.restaurant.domain.model.RestaurantTemplate;
import org.delicias.restaurant.domain.repository.RestaurantTemplateRepository;
import org.delicias.restaurant.dto.RestaurantAddressDTO;
import org.locationtech.jts.geom.Point;

import java.util.Optional;

@ApplicationScoped
public class RestaurantTemplateAddressService {

    @Inject
    RestaurantTemplateRepository repository;

    public RestaurantAddressDTO findAddress(Integer restaurantTmplId) {

        RestaurantTemplate restaurantTemplate = repository.findById(restaurantTmplId);

        if(restaurantTemplate == null) {
            throw new NotFoundException("Restaurant Tmpl Not Found");
        }

        return modelToRestaurantAddressDTO(restaurantTemplate);
    }


    @Transactional
    public void updateAddress(RestaurantAddressDTO req) {

        RestaurantTemplate restaurantTemplate = repository.findById(req.id());

        if(restaurantTemplate == null) {
            throw new NotFoundException("Restaurant Tmpl Not Found");
        }

        restaurantTemplate.setAddress(req.address());
        restaurantTemplate.updatePosition(req.longitude(), req.latitude());

    }


    private RestaurantAddressDTO modelToRestaurantAddressDTO(RestaurantTemplate model) {
        return RestaurantAddressDTO.builder()
                .id(model.getId())
                .address(model.getAddress())
                .latitude(Optional.ofNullable(model.getPosition())
                        .map(Point::getY)
                        .orElse(null))
                .longitude(Optional.ofNullable(model.getPosition())
                        .map(Point::getX)
                        .orElse(null))
                .build();
    }
}
