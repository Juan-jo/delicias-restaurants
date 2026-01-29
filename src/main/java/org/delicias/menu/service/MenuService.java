package org.delicias.menu.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.delicias.menu.domain.model.RestaurantMenu;
import org.delicias.menu.domain.repository.MenuRepository;
import org.delicias.menu.dto.MenuDTO;
import org.delicias.restaurant.domain.model.RestaurantTemplate;

import java.util.List;

@ApplicationScoped
public class MenuService {

    @Inject
    MenuRepository repository;

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

    public List<MenuDTO> findByRestaurant(Integer restaurantTmplId) {

        return repository.findByRestaurantTmplId(restaurantTmplId)
                .stream().map(it -> MenuDTO.builder()
                        .id(it.getId())
                        .name(it.getName())
                        .sequence(it.getSequence())
                        .available(it.isAvailable())
                        .build()).toList();
    }


    @Transactional
    public void deleteById(Integer menuId) {

        var deleted = repository.deleteById(menuId);

        if (!deleted) {
            throw new NotFoundException("Menu Not Found");
        }
    }
}
