package org.delicias.mobile.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.delicias.mobile.service.RestaurantDetailService;

@Path("/api/restaurants/{restaurantId}/mobile")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RestaurantDetailResource {

    @Inject
    RestaurantDetailService service;

    @GET
    public Response findMenus(
            @PathParam("restaurantId") Integer restaurantId
    ) {
        return Response.ok(
                service.findDetail(restaurantId)
        ).build();
    }
}
