package org.delicias.scheduled.domain.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.delicias.scheduled.domain.model.RestaurantScheduled;

import java.util.List;

@ApplicationScoped
public class RestaurantScheduledRepository implements PanacheRepositoryBase<RestaurantScheduled, Integer> {

    public List<RestaurantScheduled> findByRestaurantId(Integer restaurantTmplId) {
        return find("restaurantTmpl.id", restaurantTmplId).list();
    }
}
