package org.delicias.menu_products.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.delicias.menu.domain.model.RestaurantMenu;
import org.delicias.menu_products.domain.model.MenuProduct;
import org.delicias.menu_products.domain.repository.MenuProductRepository;
import org.delicias.menu_products.dto.CreateMenuProductDTO;

@ApplicationScoped
public class MenuProductService {

    @Inject
    MenuProductRepository repository;


    @Transactional
    public void create(Integer menuId, CreateMenuProductDTO req) {

        repository.persist(
                MenuProduct.builder()
                        .menu(new RestaurantMenu(menuId))
                        .productTmplId(req.productTmplId())
                        .sequence(req.sequence())
                        .build()
        );
    }

    @Transactional
    public void deleteById(Integer menuProductId) {

        var deleted = repository.deleteById(menuProductId);

        if (!deleted) {
            throw new NotFoundException("Menu Product Not Found");
        }
    }



}
