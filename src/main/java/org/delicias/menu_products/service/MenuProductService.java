package org.delicias.menu_products.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.delicias.common.dto.ProductResumeDTO;
import org.delicias.menu.domain.model.RestaurantMenu;
import org.delicias.menu_products.domain.model.MenuProduct;
import org.delicias.menu_products.domain.repository.MenuProductRepository;
import org.delicias.menu_products.dto.CreateMenuProductDTO;
import org.delicias.menu_products.dto.MenuProductDTO;
import org.delicias.rest.clients.ProductClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class MenuProductService {

    @Inject
    MenuProductRepository repository;

    @Inject
    @RestClient
    ProductClient productClient;

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

    public MenuProductDTO findById(Integer menuProductId) {
        var entity = repository.findById(menuProductId);

        if (entity == null) {
            throw new NotFoundException("Menu Product Not Found");
        }

        ProductResumeDTO product = productClient.getProductsByIds(
                        List.of(entity.getProductTmplId())
                )
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Product Not Found"));

        return MenuProductDTO.builder()
                .id(entity.getId())
                .sequence(entity.getSequence())
                .product(MenuProductDTO.Product.builder()
                        .name(product.name())
                        .description(product.description())
                        .listPrice(product.listPrice())
                        .picture(product.pictureUrl())
                        .build())
                .build();
    }

    @Transactional
    public void patch(Integer menuProductId, Map<String, Object> data) {

        var entity = repository.findById(menuProductId);

        if (entity == null) {
            throw new NotFoundException("MenuProduct Not Found");
        }

        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        MenuProduct patched = mapper.convertValue(data, MenuProduct.class);

        if (patched.getSequence() != null) {
            entity.setSequence(patched.getSequence());
        }

    }

    @Transactional
    public void deleteById(Integer menuProductId) {

        var deleted = repository.deleteById(menuProductId);

        if (!deleted) {
            throw new NotFoundException("Menu Product Not Found");
        }
    }



}
