package org.delicias.business.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.delicias.business.domain.model.BusinessCategoryRel;
import org.delicias.business.dto.MobileBusinessCategDTO;
import org.delicias.common.dto.PagedResult;
import org.delicias.minio.MinioStorageService;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Optional;

@ApplicationScoped
public class MobileBusinessCategoryService {

    @Inject
    MinioStorageService minioStorageService;

    @ConfigProperty(name = "delicias.defaultLogo")
    String defaultLogo;

    public PagedResult<MobileBusinessCategDTO> getByZoneBusinessCateg(
            Integer zoneBusinessCategId,
            int page,
            int size
    ) {

        PagedResult<BusinessCategoryRel> paged = BusinessCategoryRel.getByZoneBusinessCategId(
                zoneBusinessCategId,
                null,
                page,
                size
        );

        return new PagedResult<>(
                paged.data().stream().map(it -> new MobileBusinessCategDTO(
                        it.getRestaurantTmpl().getId(),
                        it.getRestaurantTmpl().getName(),
                        minioStorageService.fitThumbnailUrl(
                                Optional.ofNullable(it.getRestaurantTmpl().getImageLogo()).orElse(defaultLogo)
                        ),
                        Optional.ofNullable(it.getRestaurantTmpl().getAddress()).orElse("--")
                )).toList(),
                paged.total(),
                paged.page(),
                paged.size());
    }
}
