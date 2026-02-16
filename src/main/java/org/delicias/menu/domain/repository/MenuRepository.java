package org.delicias.menu.domain.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.delicias.menu.domain.model.RestaurantMenu;

import java.util.List;

@ApplicationScoped
public class MenuRepository implements PanacheRepositoryBase<RestaurantMenu, Integer> {

    private final String queryFilterByRestaurant = "restaurantTmpl.id = ?1";


    public List<RestaurantMenu> findByRestaurantTmplId(
            Integer restaurantTmplId,
            int page,
            int size) {

        return find(queryFilterByRestaurant, Sort.ascending("sequence"), restaurantTmplId)
                .page(Page.of(page, size))
                .list();
    }

    public long countByRestaurant(Integer restaurantTmplId) {
        return count(queryFilterByRestaurant, restaurantTmplId);
    }


}
