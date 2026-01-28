package org.delicias.scheduled.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.delicias.common.validation.OnUpdate;
import org.delicias.scheduled.dto.RestaurantScheduleDTO;
import org.delicias.scheduled.service.RestaurantScheduleService;

import java.util.List;

@Path("/api/restaurants/{restaurantId}/schedules")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RestaurantScheduleResource {

    @Inject
    RestaurantScheduleService service;

    @GET
    public List<RestaurantScheduleDTO> getSchedules(
            @PathParam("restaurantId") Integer restaurantId) {

        return service.findByRestaurant(restaurantId);
    }

    @PUT
    public List<RestaurantScheduleDTO> update(
            @PathParam("restaurantId") Integer restaurantId,
            List<@Valid @ConvertGroup(to = OnUpdate.class) RestaurantScheduleDTO> schedules) {

        return service.updateSchedules(schedules);
    }
}
