package org.delicias.scheduled.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.delicias.restaurant.domain.model.RestaurantTemplate;
import org.delicias.scheduled.domain.model.RestaurantScheduled;
import org.delicias.scheduled.domain.repository.RestaurantScheduledRepository;
import org.delicias.scheduled.dto.RestaurantScheduleDTO;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class RestaurantScheduleService {

    @Inject
    RestaurantScheduledRepository repository;


    @Transactional
    public List<RestaurantScheduleDTO> findByRestaurant(Integer restaurantTmplId) {

        List<RestaurantScheduled> schedules = repository.findByRestaurantId(restaurantTmplId);

        if (schedules.isEmpty()) {
            schedules = generateSchedules(restaurantTmplId);
        }

        return schedules.stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Transactional
    public List<RestaurantScheduleDTO> updateSchedules(List<RestaurantScheduleDTO> dtos) {
        List<RestaurantScheduled> updatedEntities = new ArrayList<>();

        for (RestaurantScheduleDTO dto : dtos) {

            RestaurantScheduled entity = repository.findById(dto.id());

            if (entity != null) {

                entity.setStartTime(dto.startTime());
                entity.setEndTime(dto.endTime());
                entity.setAvailable(dto.available());

                updatedEntities.add(entity);
            }
        }

        return updatedEntities.stream()
                .map(this::mapToDTO)
                .toList();
    }


    private List<RestaurantScheduled> generateSchedules(Integer restaurantTmplId) {

        List<RestaurantScheduled> newSchedules = Arrays.stream(DayOfWeek.values())
                .map(day -> RestaurantScheduled.builder()
                        .restaurantTmpl(new RestaurantTemplate(restaurantTmplId))
                        .dayOfWeek(day)
                        .startTime(LocalTime.MIN) // 00:00
                        .endTime(LocalTime.MIN)   // 00:00
                        .available(false)
                        .build())
                .toList();

        repository.persist(newSchedules);
        return newSchedules;
    }

    private RestaurantScheduleDTO mapToDTO(RestaurantScheduled entity) {
        return RestaurantScheduleDTO.builder()
                .id(entity.getId())
                .dayOfWeek(entity.getDayOfWeek())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .available(entity.getAvailable())
                .build();
    }

}
