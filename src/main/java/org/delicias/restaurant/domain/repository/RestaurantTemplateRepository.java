package org.delicias.restaurant.domain.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.delicias.restaurant.domain.model.RestaurantTemplate;

@ApplicationScoped
public class RestaurantTemplateRepository implements PanacheRepositoryBase<RestaurantTemplate, Integer> {

}
