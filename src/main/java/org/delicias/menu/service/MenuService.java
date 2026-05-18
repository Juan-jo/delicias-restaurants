package org.delicias.menu.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.delicias.common.dto.PagedResult;
import org.delicias.common.dto.product.ProductResumeDTO;
import org.delicias.menu.domain.model.RestaurantMenu;
import org.delicias.menu.domain.repository.MenuRepository;
import org.delicias.menu.dto.MenuDTO;
import org.delicias.menu_products.domain.model.MenuProduct;
import org.delicias.menu_products.domain.repository.MenuProductRepository;
import org.delicias.minio.MinioStorageService;
import org.delicias.rest.clients.ProductClient;
import org.delicias.restaurant.domain.model.RestaurantTemplate;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class MenuService {

    @Inject
    MenuRepository repository;

    @Inject
    MenuProductRepository menuProductRepository;

    @Inject
    @RestClient
    ProductClient productClient;

    @Inject
    MinioStorageService minioStorageService;

    @Transactional
    public void create(Integer restaurantTmplId, MenuDTO req) {

        repository.persist(RestaurantMenu.builder()
                .restaurantTmpl(new RestaurantTemplate(restaurantTmplId))
                .name(req.name())
                .sequence(req.sequence())
                .available(req.available())
                .build());
    }

    @Transactional
    public void update(MenuDTO req) {

        var entity = repository.findById(req.id());

        if(entity == null) {
            throw new NotFoundException("Menu Not Found");
        }

        entity.setName(req.name());
        entity.setSequence(req.sequence());
        entity.setAvailable(req.available());
    }

    public MenuDTO findById(Integer menuId) {

        var entity = repository.findById(menuId);

        if (entity == null) {
            throw new NotFoundException("Menu Not Found");
        }

        return MenuDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .sequence(entity.getSequence())
                .available(entity.isAvailable())
                .build();
    }

    public PagedResult<MenuDTO> findByRestaurant(Integer restaurantTmplId, Integer page, Integer size) {

        List<RestaurantMenu> menus = repository.findByRestaurantTmplId(restaurantTmplId, page, size);

        long total = repository.countByRestaurant(restaurantTmplId);

        if (total == 0 || menus.isEmpty()) {
            return new PagedResult<>(
                    List.of(),
                    total,
                    page,
                    size
            );
        }

        var menusId = menus.stream().map(RestaurantMenu::getId).toList();

        List<MenuProduct> menuProducts = menuProductRepository.findByMenuIds(
                menusId
        );

        var productsId = menuProducts.stream()
                .map(MenuProduct::getProductTmplId)
                .collect(Collectors.toSet());

        List<ProductResumeDTO> productsDetail = productClient.getProductsByIds(
                productsId
        );

        Map<Integer, ProductResumeDTO> productMap = productsDetail.stream()
                .collect(Collectors.toMap(ProductResumeDTO::id, p -> p));

        var filtered = menus.stream().map(menu -> {

            List<MenuDTO.ProductDTO> productsForThisMenu = menuProducts.stream()
                    .filter(mp -> mp.getMenu().getId().equals(menu.getId()))
                    .map(mp -> {

                        var prod = productMap.get(mp.getProductTmplId());

                        if (prod == null) return null;

                        return MenuDTO.ProductDTO.builder()
                                .id(mp.getId())
                                .productTmplId(mp.getProductTmplId())
                                .name(prod.name())
                                .description(prod.description())
                                .listPrice(prod.listPrice())
                                .pictureUrl(
                                        minioStorageService.thumbnailUrl(prod.pictureUrl())
                                )
                                .sequence(mp.getSequence())
                                .build();
                    })
                    .filter(Objects::nonNull)
                    .toList();

            return MenuDTO.builder()
                    .id(menu.getId())
                    .name(menu.getName())
                    .sequence(menu.getSequence())
                    .available(menu.isAvailable())
                    .products(productsForThisMenu)
                    .build();
        }).toList();

        return new PagedResult<>(
                filtered,
                total,
                page,
                size
        );
    }


    @Transactional
    public void deleteById(Integer menuId) {

        var deleted = repository.deleteById(menuId);

        if (!deleted) {
            throw new NotFoundException("Menu Not Found");
        }
    }
}
