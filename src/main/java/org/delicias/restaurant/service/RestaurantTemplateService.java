package org.delicias.restaurant.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.delicias.common.dto.PagedResult;
import org.delicias.restaurant.domain.model.RestaurantTemplate;
import org.delicias.restaurant.domain.repository.RestaurantTemplateRepository;
import org.delicias.restaurant.dto.RestaurantFilterItemDTO;
import org.delicias.restaurant.dto.RestaurantFilterReqDTO;
import org.delicias.restaurant.dto.RestaurantTemplateDTO;
import org.delicias.supabase.SupabaseStorageService;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@ApplicationScoped
public class RestaurantTemplateService {

    @Inject
    SupabaseStorageService storageService;

    @Inject
    RestaurantTemplateRepository repository;

    @ConfigProperty(name = "delicias.defaultLogo")
    String defaultLogo;

    @Transactional
    public void create(RestaurantTemplateDTO templateDTO) {

        RestaurantTemplate restaurantTemplate = RestaurantTemplate.builder()
                .name(templateDTO.name())
                .description(templateDTO.description())
                .phone(templateDTO.phone())
                .build();

        repository.persist(restaurantTemplate);
    }

    @Transactional
    public void update(RestaurantTemplateDTO templateDTO) {

        RestaurantTemplate restaurantTemplate = repository.findById(templateDTO.id());

        if(restaurantTemplate == null) {
            throw new NotFoundException("Restaurant Tmpl Not Found");
        }

        restaurantTemplate.update(templateDTO);
    }

    public RestaurantTemplateDTO findById(Integer id) {

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
                        .picture(Optional.ofNullable(it.getImageLogo()).orElse(defaultLogo))
                        .createdAt(it.getCreatedAt())
                        .updatedAt(it.getUpdatedAt())
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
    public Map<String, String> uploadLogo(Integer restaurantTmplId, FileUpload logoFile) throws IOException {

        RestaurantTemplate restaurantTemplate = repository.findById(restaurantTmplId);

        if(restaurantTemplate == null) {
            throw new NotFoundException("Restaurant Tmpl Not Found");
        }

        String logoUrl = storageService.uploadFile(logoFile);

        deleteCurrentPicture(restaurantTemplate.getImageLogo());

        restaurantTemplate.setImageLogo(logoUrl);

        return Map.of("picture", logoUrl);
    }

    @Transactional
    public Map<String, String> uploadCover(Integer restaurantTmplId, FileUpload logoFile) throws IOException {

        RestaurantTemplate restaurantTemplate = repository.findById(restaurantTmplId);

        if(restaurantTemplate == null) {
            throw new NotFoundException("Restaurant Tmpl Not Found");
        }

        String logoUrl = storageService.uploadFile(logoFile);

        deleteCurrentPicture(restaurantTemplate.getImageCover());

        restaurantTemplate.setImageCover(logoUrl);

        return Map.of("picture", logoUrl);
    }


    private void deleteCurrentPicture(String pictureUrl) {
        if(Optional.ofNullable(pictureUrl).isPresent()) {
            storageService.deleteFile(pictureUrl);
        }
    }



    private RestaurantTemplateDTO modelToRestaurantTemplateDTO(RestaurantTemplate template) {

        return RestaurantTemplateDTO.builder()
                .id(template.getId())
                .name(template.getName())
                .description(template.getDescription())
                .phone(template.getPhone())
                .logoPicture(
                        Optional.ofNullable(template.getImageLogo())
                                .orElse("")
                )
                .build();
    }

}
