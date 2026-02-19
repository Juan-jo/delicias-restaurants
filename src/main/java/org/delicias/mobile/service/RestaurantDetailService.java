package org.delicias.mobile.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.delicias.common.dto.product.ProductResumeDTO;
import org.delicias.menu.domain.repository.MenuRepository;
import org.delicias.menu_products.domain.model.MenuProduct;
import org.delicias.menu_products.domain.repository.MenuProductRepository;
import org.delicias.mobile.dto.RestaurantDetailDTO;
import org.delicias.products_recommend.domain.model.ProductRecommend;
import org.delicias.products_recommend.domain.repository.ProductRecommendRepository;
import org.delicias.rest.clients.ProductClient;
import org.delicias.restaurant.domain.model.RestaurantTemplate;
import org.delicias.restaurant.domain.repository.RestaurantTemplateRepository;
import org.delicias.scheduled.domain.model.RestaurantScheduled;
import org.delicias.scheduled.domain.repository.RestaurantScheduledRepository;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class RestaurantDetailService {

    @ConfigProperty(name = "delicias.defaultLogo")
    String defaultLogo;

    @Inject
    RestaurantTemplateRepository templateRepository;

    @Inject
    RestaurantScheduledRepository scheduledRepository;

    @Inject
    MenuRepository menuRepository;

    @Inject
    ProductRecommendRepository recommendRepository;


    @Inject
    @RestClient
    ProductClient productClient;

    public RestaurantDetailDTO findDetail(Integer restaurantTmplId) {

        RestaurantTemplate restaurant = templateRepository.findById(restaurantTmplId);

        if (restaurant == null) {
            throw new NotFoundException("RestaurantTemplate Not Found");
        }

        LocalTime timeNow = LocalTime.now();
        RestaurantScheduled scheduled = getScheduled(restaurantTmplId);

        var allMenus = menuRepository.findByRestaurantTmplId(restaurantTmplId);
        var allRecommended = recommendRepository.findByRestaurant(restaurantTmplId);

        Set<Integer> productIds = Stream.concat(
                allMenus.stream()
                        .flatMap(it -> it.getProducts().stream())
                        .map(MenuProduct::getProductTmplId),
                allRecommended.stream().map(ProductRecommend::getProductTmplId)
        ).collect(Collectors.toSet());

        Map<Integer, ProductResumeDTO> productsMap = productClient.getProductsByIds(
                productIds
        ).stream().collect(Collectors.toMap(ProductResumeDTO::id, p -> p));

        return RestaurantDetailDTO.builder()
                .id(restaurant.getId())
                .name(restaurant.getImageCover())
                .imageCoverUrl(Optional.ofNullable(restaurant.getImageCover()).orElse(defaultLogo))
                .info(RestaurantDetailDTO.RestaurantInfo.builder()
                        .imageLogoUrl(Optional.ofNullable(restaurant.getImageLogo()).orElse(defaultLogo))
                        .hourStart(scheduled.getStartTime())
                        .hourEnd(scheduled.getEndTime())
                        .available(timeNow.isAfter(scheduled.getStartTime()) && timeNow.isBefore(scheduled.getEndTime()))
                        .address(Optional.ofNullable(restaurant.getAddress()).orElse(""))
                        .build())
                .recommended(
                        allRecommended.stream().map(it -> {

                                    var product = productsMap.get(it.getProductTmplId());

                                    if (product == null) return null;

                                    return RestaurantDetailDTO.RecommendedItem.builder()
                                            .id(product.id())
                                            .picture(product.pictureUrl())
                                            .name(product.name())
                                            .priceList(product.listPrice())
                                            .build();
                                }).filter(Objects::nonNull)
                                .toList()
                )
                .menu(
                        allMenus.stream().map(m -> RestaurantDetailDTO.Menu.builder()
                                .name(m.getName())
                                .products(
                                        m.getProducts().stream().map(p -> {
                                            var product = productsMap.get(p.getProductTmplId());

                                            if (product == null) return null;

                                            return RestaurantDetailDTO.ProductMenu.builder()
                                                    .id(product.id())
                                                    .picture(product.pictureUrl())
                                                    .name(product.name())
                                                    .priceList(product.listPrice())
                                                    .build();

                                        }).filter(Objects::nonNull).toList()
                                )
                                .build()).filter(m -> !m.products().isEmpty()).toList()
                )
                .build();
    }

    private RestaurantScheduled getScheduled(Integer restaurantTmplId) {
        DayOfWeek today = LocalDateTime.now().getDayOfWeek();
        return scheduledRepository.findByRestaurantId(restaurantTmplId)
                .stream().filter(it->it.getDayOfWeek().equals(today))
                .findAny()
                .orElseThrow(() -> new NotFoundException("RestaurantScheduled Not Found"));
    }

}
