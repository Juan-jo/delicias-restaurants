package org.delicias.menu.domain.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.delicias.menu.domain.model.RestaurantMenu;

import java.util.List;

@ApplicationScoped
public class MenuRepository implements PanacheRepositoryBase<RestaurantMenu, Integer> {

    public List<RestaurantMenu> findByRestaurantTmplId(Integer restaurantTmplId) {
        return list("restaurantTmpl.id", Sort.ascending("sequence"), restaurantTmplId);
    }
}
