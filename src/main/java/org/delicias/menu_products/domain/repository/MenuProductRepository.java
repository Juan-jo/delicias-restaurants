package org.delicias.menu_products.domain.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.delicias.menu_products.domain.model.MenuProduct;

import java.util.List;

@ApplicationScoped
public class MenuProductRepository implements PanacheRepositoryBase<MenuProduct, Integer> {

    public List<MenuProduct> findByMenuId(Integer menuId) {
        return list("menu.id", Sort.ascending("sequence"), menuId);
    }
}
