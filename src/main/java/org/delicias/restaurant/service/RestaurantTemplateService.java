package org.delicias.restaurant.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.delicias.common.dto.PagedResult;
import org.delicias.common.dto.restaurant.RestaurantLatLngDTO;
import org.delicias.common.dto.restaurant.RestaurantResumeDTO;
import org.delicias.minio.MinioStorageService;
import org.delicias.minio.utils.MinioRS;
import org.delicias.minio.utils.MinioSize;
import org.delicias.restaurant.domain.model.RestaurantTemplate;
import org.delicias.restaurant.domain.repository.RestaurantTemplateRepository;
import org.delicias.restaurant.dto.RestaurantFilterItemDTO;
import org.delicias.restaurant.dto.RestaurantFilterReqDTO;
import org.delicias.restaurant.dto.RestaurantTemplateDTO;
import org.delicias.restaurant.dto.RestaurantTemplateSummaryDTO;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.locationtech.jts.geom.Point;

import java.io.IOException;
import java.util.*;


@ApplicationScoped
public class RestaurantTemplateService {

    @Inject
    MinioStorageService minioStorageService;

    @Inject
    RestaurantTemplateRepository repository;

    @ConfigProperty(name = "delicias.defaultLogo")
    String defaultLogo;

    @Transactional
    public void create(RestaurantTemplateSummaryDTO templateDTO) {

        RestaurantTemplate restaurantTemplate = RestaurantTemplate.builder()
                .name(templateDTO.name())
                .description(templateDTO.description())
                .phone(templateDTO.phone())
                .storeType(templateDTO.storeType())
                .build();

        repository.persist(restaurantTemplate);
    }

    @Transactional
    public void update(RestaurantTemplateSummaryDTO templateDTO) {

        RestaurantTemplate restaurantTemplate = repository.findById(templateDTO.id());

        if(restaurantTemplate == null) {
            throw new NotFoundException("Restaurant Tmpl Not Found");
        }

        restaurantTemplate.update(templateDTO);
    }

    public RestaurantTemplateSummaryDTO findSummaryById(Integer id) {

        RestaurantTemplate restaurantTemplate = repository.findById(id);

        if(restaurantTemplate == null) {
            throw new NotFoundException("Restaurant Tmpl Not Found");
        }

        return modelToRestaurantTemplateSummaryDTO(restaurantTemplate);
    }

    public RestaurantTemplateDTO findFullById(Integer id) {

        RestaurantTemplate restaurantTemplate = repository.findById(id);

        if(restaurantTemplate == null) {
            throw new NotFoundException("Restaurant Tmpl Not Found");
        }

        return modelToRestaurantTemplateDTO(restaurantTemplate);
    }

    @Transactional
    public void deleteById(Integer id) {
        var deleted = repository.deleteById(id);

        if (!deleted) {
            throw new NotFoundException("Restaurant Tmpl Not Found");
        }
    }

    public PagedResult<RestaurantFilterItemDTO> filterSearch(
            RestaurantFilterReqDTO req
    ) {

        List<RestaurantFilterItemDTO> filtered = repository.searchByName(
                        req.getName(),
                        req.getPage(),
                        req.getSize(),
                        req.getOrderColumn(),
                        req.toOrderDirection()
                )
                .stream().map(it -> RestaurantFilterItemDTO.builder()
                        .id(it.getId())
                        .name(it.getName())
                        .picture(minioStorageService.pictureUrl(
                                Optional.ofNullable(it.getImageLogo()).orElse(defaultLogo),
                                MinioSize.SMALL,
                                MinioRS.FIT,
                                (short) 70
                        ))
                        .storeType(it.getStoreType().getDescription())
                        .address(Optional.ofNullable(it.getAddress()).orElse("--"))
                        .build()).toList();

        long total = repository.countByName(req.getName());

        return new PagedResult<>(
                filtered,
                total,
                req.getPage(),
                req.getSize()
        );

    }


    @Transactional
    public Map<String, String> uploadLogo(Integer restaurantTmplId, FileUpload file) throws IOException {

        RestaurantTemplate restaurantTemplate = repository.findById(restaurantTmplId);

        if(restaurantTemplate == null) {
            throw new NotFoundException("Restaurant Tmpl Not Found");
        }

        String logoUrl = minioStorageService.upload(
                file
        );

        restaurantTemplate.setImageLogo(logoUrl);

        return Map.of("picture",
                minioStorageService.pictureUrl(
                        logoUrl,
                        MinioSize.MEDIUM,
                        MinioRS.FIT,
                        (short) 70
                ));
    }

    @Transactional
    public Map<String, String> uploadCover(Integer restaurantTmplId, FileUpload file) throws IOException {

        RestaurantTemplate restaurantTemplate = repository.findById(restaurantTmplId);

        if(restaurantTemplate == null) {
            throw new NotFoundException("Restaurant Tmpl Not Found");
        }

        String logoUrl = minioStorageService.upload(file);

        restaurantTemplate.setImageCover(logoUrl);

        return Map.of("picture",
                minioStorageService.pictureUrl(
                        logoUrl,
                        MinioSize.BANNER_CE,
                        MinioRS.FIT,
                        (short) 70
                ));
    }

    public List<RestaurantResumeDTO> findByIds(List<Integer> ids) {

        return repository.findByIds(ids)
                .stream()
                .map(it -> RestaurantResumeDTO.builder()
                        .id(it.getId())
                        .name(it.getName())
                        .description(Optional.ofNullable(it.getDescription()).orElse(""))
                        .logoUrl(Optional.ofNullable(it.getImageLogo()).orElse(defaultLogo))
                        .coverUrl(Optional.ofNullable(it.getImageCover()).orElse(defaultLogo))
                        .address(Optional.ofNullable(it.getAddress()).orElse("Desconocido"))
                        .storeType(it.getStoreType())
                        .build()).toList();
    }

    public RestaurantLatLngDTO getLatLng(Integer restaurantTmplId) {

        RestaurantTemplate restaurant = repository.findById(restaurantTmplId);

        if(restaurant == null) {
            throw new NotFoundException("Restaurant Tmpl Not Found");
        }

        return new RestaurantLatLngDTO(
                Optional.ofNullable(restaurant.getPosition())
                        .map(Point::getY).orElse(Double.NaN),
                Optional.ofNullable(restaurant.getPosition())
                        .map(Point::getX).orElse(Double.NaN)
        );

    }

    public Map<String, Object> findWithFields(Integer restaurantTmplId, String fields) {

        Set<String> fieldSet = fields != null
                ? new HashSet<>(Arrays.asList(fields.split(",")))
                : null;

        RestaurantTemplate restaurantTemplate = repository.findById(restaurantTmplId);

        if(restaurantTemplate == null) {
            throw new NotFoundException("Restaurant Tmpl Not Found");
        }

        return filterFields(restaurantTemplate, fieldSet);
    }

    private Map<String, Object> filterFields(RestaurantTemplate r, Set<String> fields) {
        Map<String, Object> map = new HashMap<>();

        if(fields != null) {

            if (fields.contains("id"))
                map.put("id", r.getId());

            if (fields.contains("name"))
                map.put("name", r.getName());

            if (fields.contains("latitude"))
                map.put("latitude", Optional.ofNullable(r.getPosition())
                        .map(Point::getY).orElse(Double.NaN));

            if (fields.contains("longitude"))
                map.put("longitude", Optional.ofNullable(r.getPosition())
                        .map(Point::getX).orElse(Double.NaN));

            if (fields.contains("address"))
                map.put("address", r.getAddress());

            if (fields.contains("photo"))
                map.put("photo", r.getImageLogo());

            if (fields.contains("cover"))
                map.put("cover", r.getImageCover());
        }

        return map;
    }





    private RestaurantTemplateSummaryDTO modelToRestaurantTemplateSummaryDTO(RestaurantTemplate template) {

        return RestaurantTemplateSummaryDTO.builder()
                .id(template.getId())
                .name(template.getName())
                .description(template.getDescription())
                .phone(template.getPhone())
                .logoPicture(
                        minioStorageService.pictureUrl(
                                Optional.ofNullable(template.getImageLogo()).orElse(defaultLogo),
                                MinioSize.MEDIUM,
                                MinioRS.FILL,
                                (short) 70
                        )
                )
                .storeType(template.getStoreType())
                .build();
    }


    private RestaurantTemplateDTO modelToRestaurantTemplateDTO(RestaurantTemplate template) {

        return RestaurantTemplateDTO.builder()
                .id(template.getId())
                .name(template.getName())
                .description(template.getDescription())
                .phone(template.getPhone())
                .address(template.getAddress())
                .logoPicture(minioStorageService.pictureUrl(
                        Optional.ofNullable(template.getImageLogo()).orElse(defaultLogo),
                        MinioSize.MEDIUM,
                        MinioRS.FIT,
                        (short) 70)
                )
                .coverPicture(
                        minioStorageService.pictureUrl(
                                Optional.ofNullable(template.getImageCover()).orElse(defaultLogo),
                                MinioSize.BANNER_CE,
                                MinioRS.FILL,
                                (short) 70
                        )
                )
                .storeType(template.getStoreType())
                .build();
    }





}
