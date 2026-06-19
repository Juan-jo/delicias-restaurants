package org.delicias.restaurant.service;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.delicias.common.dto.PagedResult;
import org.delicias.minio.MinioStorageService;
import org.delicias.minio.utils.MinioRS;
import org.delicias.minio.utils.MinioSize;
import org.delicias.restaurant.domain.repository.RestaurantTemplateRepository;
import org.delicias.restaurant.dto.RestaurantTmplOptionDTO;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class SearchRestaurantTmplService {

    @Inject
    RestaurantTemplateRepository repository;

    @Inject
    MinioStorageService storageService;

    @ConfigProperty(name = "delicias.defaultLogo")
    String defaultLogo;

    public PagedResult<RestaurantTmplOptionDTO> search(
            String name,
            int page,
            int size
    ) {

        List<RestaurantTmplOptionDTO> filtered = repository.searchByName(
                        name,
                        page,
                        size,
                        "name",
                        Sort.Direction.Ascending
                )
                .stream().map(it -> new RestaurantTmplOptionDTO(
                        it.getId(),
                        it.getName(),
                        it.getStoreType().getDescription(),
                        Optional.ofNullable(it.getAddress()).orElse("--"),
                        storageService.pictureUrl(Optional.ofNullable(it.getImageLogo())
                                .orElse(defaultLogo),
                                MinioSize.SMALL,
                                MinioRS.FIT,
                                (short)70)
                ))
                .toList();

        long total = repository.countByName(name);

        return new PagedResult<>(
                filtered,
                total,
                page,
                size
        );

    }
}
