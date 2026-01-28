package org.delicias.products_recommend.domain.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.delicias.products_recommend.domain.model.ProductRecommend;

import java.util.List;

@ApplicationScoped
public class ProductRecommendRepository implements PanacheRepositoryBase<ProductRecommend, Integer> {

    public List<ProductRecommend> findByRestaurantTmplId(Integer restaurantTmplId) {
        return list("restaurantTmpl.id", Sort.ascending("sequence"), restaurantTmplId);
    }

}
