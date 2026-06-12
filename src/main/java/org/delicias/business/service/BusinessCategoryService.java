package org.delicias.business.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.delicias.business.domain.model.BusinessCategoryRel;
import org.delicias.business.dto.BusinessCategoryRelDTO;
import org.delicias.business.dto.BusinessCategoryRelItemDTO;
import org.delicias.business.dto.BusinessCategoryReqDTO;
import org.delicias.common.dto.PagedResult;
import org.delicias.minio.MinioStorageService;
import org.delicias.restaurant.domain.model.RestaurantTemplate;
import org.delicias.restaurant.domain.repository.RestaurantTemplateRepository;
import org.delicias.restaurant.dto.RestaurantTmplOptionDTO;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.*;

@ApplicationScoped
public class BusinessCategoryService {

    @Inject
    RestaurantTemplateRepository restaurantTemplateRepository;

    @Inject
    MinioStorageService minioStorageService;

    @ConfigProperty(name = "delicias.defaultLogo")
    String defaultLogo;

    @Transactional
    public void create(BusinessCategoryReqDTO req) {

        RestaurantTemplate restaurant = restaurantTemplateRepository.findByIdOptional(req.restaurantId())
                .orElseThrow(() -> new NotFoundException("Restaurant Not Found"));

        BusinessCategoryRel.builder()
                .zoneBusinessCategoryId(req.zoneBusinessCategoryId())
                .restaurantTmpl(restaurant)
                .sequence(req.sequence())
                .active(req.active())
                .build()
                .persist();
    }

    @Transactional
    public void update(BusinessCategoryReqDTO req) {

        BusinessCategoryRel model = BusinessCategoryRel.<BusinessCategoryRel>findByIdOptional(req.id())
                .orElseThrow(() -> new NotFoundException("BusinessCategory Not Found"));

        model.setActive(req.active());
        model.setSequence(req.sequence());
    }

    @Transactional
    public void deleteById(Integer id) {

        BusinessCategoryRel model = BusinessCategoryRel.<BusinessCategoryRel>findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("BusinessCategory Not Found"));

        model.delete();
    }

    public BusinessCategoryRelDTO findById(Integer id) {

        BusinessCategoryRel model = BusinessCategoryRel.<BusinessCategoryRel>findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("BusinessCategory Not Found"));

        return new BusinessCategoryRelDTO(
                model.getId(),
                model.getSequence(),
                model.getActive(),
                new RestaurantTmplOptionDTO(
                        model.getRestaurantTmpl().getId(),
                        model.getRestaurantTmpl().getName(),
                        model.getRestaurantTmpl().getStoreType().getDescription(),
                        Optional.ofNullable(model.getRestaurantTmpl().getAddress()).orElse("--"),
                        minioStorageService.fitThumbnailUrl(
                                Optional.ofNullable(model.getRestaurantTmpl().getImageLogo()).orElse(defaultLogo)
                        )
                )
        );
    }

    public PagedResult<BusinessCategoryRelItemDTO> listByZoneBusinessCateg(
            Integer zoneBusinessCategId,
            String name,
            int page,
            int size) {

        PagedResult<BusinessCategoryRel> paged = BusinessCategoryRel.getByZoneBusinessCategId(
                zoneBusinessCategId,
                name,
                page,
                size
        );

        return new PagedResult<>(
                paged.data().stream().map(it -> new BusinessCategoryRelItemDTO(
                        it.getId(),
                        it.getSequence(),
                        it.getActive(),
                        it.getRestaurantTmpl().getName(),
                        minioStorageService.fitThumbnailUrl(
                                Optional.ofNullable(it.getRestaurantTmpl().getImageLogo()).orElse(defaultLogo)
                        ),
                        Optional.ofNullable(it.getRestaurantTmpl().getAddress()).orElse("--")
                )).toList(),
                paged.total(),
                paged.page(),
                paged.size()
        );
    }

}
