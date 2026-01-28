package org.delicias.products_recommend.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.delicias.products_recommend.domain.model.ProductRecommend;
import org.delicias.products_recommend.domain.repository.ProductRecommendRepository;
import org.delicias.products_recommend.dto.CreateProductRecommendDTO;
import org.delicias.products_recommend.dto.ProductRecommendItmDTO;
import org.delicias.restaurant.domain.model.RestaurantTemplate;

import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductRecommendService {

    @Inject
    ProductRecommendRepository repository;

    @Transactional
    public void create(Integer restaurantTmplId, CreateProductRecommendDTO req) {

        repository.persist(ProductRecommend.builder()
                        .restaurantTmpl(new RestaurantTemplate(restaurantTmplId))
                        .productTmplId(req.productTmplId())
                        .sequence(req.sequence())
                .build());

    }


    @Transactional
    public void delete(Integer productRecommendId) {

        var deleted = repository.deleteById(productRecommendId);

        if (!deleted) {
            throw new NotFoundException("ProductRecommend Not Found");
        }

    }

    public Set<ProductRecommendItmDTO> findByRestaurantTmplId(Integer restaurantTmplId) {
        return repository.findByRestaurantTmplId(restaurantTmplId)
                .stream().map(it -> ProductRecommendItmDTO.builder()
                        .id(it.getId())
                        .productName("pName")
                        .productImageUrl("http://")
                        .sequence(it.getSequence())
                        .build()
                ).collect(Collectors.toSet());
    }

}
