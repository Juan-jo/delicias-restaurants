package org.delicias.restaurant.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.delicias.restaurant.domain.model.RestaurantTemplate;
import org.delicias.restaurant.domain.repository.RestaurantTemplateRepository;
import org.delicias.restaurant.dto.RestaurantTemplateDTO;

import java.util.Map;
import java.util.Optional;
import org.jboss.resteasy.reactive.multipart.FileUpload;


@ApplicationScoped
public class RestaurantTemplateService {

    // TODO: Add connect to Supabase

    @Inject
    RestaurantTemplateRepository repository;

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



    @Transactional
    public Map<String, String> uploadLogo(Integer restaurantTmplId, FileUpload logoFile) {

        RestaurantTemplate restaurantTemplate = repository.findById(restaurantTmplId);

        if(restaurantTemplate == null) {
            throw new NotFoundException("Restaurant Tmpl Not Found");
        }

        return Map.of("picture", "fileName");
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
