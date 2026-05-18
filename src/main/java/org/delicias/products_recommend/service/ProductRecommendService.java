package org.delicias.products_recommend.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.delicias.common.dto.PagedResult;
import org.delicias.common.dto.product.ProductResumeDTO;
import org.delicias.minio.MinioStorageService;
import org.delicias.products_recommend.domain.model.ProductRecommend;
import org.delicias.products_recommend.domain.repository.ProductRecommendRepository;
import org.delicias.products_recommend.dto.CreateProductRecommendDTO;
import org.delicias.products_recommend.dto.ProductRecommendDTO;
import org.delicias.products_recommend.dto.ProductRecommendItmDTO;
import org.delicias.rest.clients.ProductClient;
import org.delicias.restaurant.domain.model.RestaurantTemplate;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductRecommendService {

    @Inject
    ProductRecommendRepository repository;

    @Inject
    @RestClient
    ProductClient productClient;

    @Inject
    MinioStorageService minioStorageService;


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

    public ProductRecommendDTO findById(Integer productRecommendId) {

        var entity = repository.findById(productRecommendId);

        if (entity == null) {
            throw new NotFoundException("ProductRecommend Not Found");
        }

        ProductResumeDTO product = productClient.getProductsByIds(
                        Set.of(entity.getProductTmplId())
                )
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Product Not Found"));


        return ProductRecommendDTO.builder()
                .id(entity.getId())
                .sequence(entity.getSequence())
                .product(ProductRecommendDTO.Product.builder()
                        .name(product.name())
                        .description(product.description())
                        .listPrice(product.listPrice())
                        .picture(minioStorageService.thumbnailUrl(product.pictureUrl()))
                        .build())
                .build();
    }

    @Transactional
    public void patch(Integer productRecommendedId, Map<String, Object> data) {

        var entity = repository.findById(productRecommendedId);

        if (entity == null) {
            throw new NotFoundException("ProductRecommend Not Found");
        }

        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        ProductRecommend patched = mapper.convertValue(data, ProductRecommend.class);

        if (patched.getSequence() != null) {
            entity.setSequence(patched.getSequence());
        }

    }

    public PagedResult<ProductRecommendItmDTO> findByRestaurant(
            Integer restaurantTmplId, Integer page, Integer size) {

        var recommends = repository.findByRestaurant(restaurantTmplId, page, size);

        long total = repository.countByRestaurant(restaurantTmplId);

        if (total == 0 || recommends.isEmpty()) {
            return new PagedResult<>(
                    List.of(),
                    total,
                    page,
                    size
            );
        }

        var productsId = recommends.stream().map(ProductRecommend::getProductTmplId)
                .collect(Collectors.toSet());

        List<ProductResumeDTO> products = productClient.getProductsByIds(
                productsId
        );

        Map<Integer, ProductResumeDTO> productMap = products.stream()
                .collect(Collectors.toMap(ProductResumeDTO::id, p -> p));

        var filtered = recommends
                .stream().map(mp -> {

                    var prod = productMap.get(mp.getProductTmplId());

                    if (prod == null) return null;

                    return ProductRecommendItmDTO.builder()
                            .id(mp.getId())
                            .productTmplId(prod.id())
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
                .collect(Collectors.toList());

        return new PagedResult<>(
                filtered,
                total,
                page,
                size
        );
    }

}
