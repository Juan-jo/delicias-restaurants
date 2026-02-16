package org.delicias.products_recommend.domain.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.delicias.products_recommend.domain.model.ProductRecommend;

import java.util.List;

@ApplicationScoped
public class ProductRecommendRepository implements PanacheRepositoryBase<ProductRecommend, Integer> {

    private final String queryFilterByRestaurant = "restaurantTmpl.id = ?1";


    public List<ProductRecommend> findByRestaurant(
            Integer restaurantTmplId,
            Integer page,
            Integer size
    ) {
        return find(queryFilterByRestaurant, Sort.ascending("sequence"), restaurantTmplId)
                .page(Page.of(page, size))
                .list();
    }

    public long countByRestaurant(Integer restaurantTmplId) {
        return count(queryFilterByRestaurant, restaurantTmplId);
    }

}
