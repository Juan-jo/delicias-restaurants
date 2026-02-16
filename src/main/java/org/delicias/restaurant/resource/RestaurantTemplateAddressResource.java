package org.delicias.restaurant.resource;


import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.delicias.common.validation.OnUpdate;
import org.delicias.restaurant.dto.RestaurantAddressDTO;
import org.delicias.restaurant.service.RestaurantTemplateAddressService;

@Authenticated
@Path("/api/restaurants/{restaurantTmplId}/address")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RestaurantTemplateAddressResource {


    @Inject
    RestaurantTemplateAddressService service;

    @GET
    public Response findById(
            @PathParam("restaurantTmplId") Integer restaurantTmplId
    ) {

        return Response.ok(
                service.findAddress(restaurantTmplId)
        ).build();
    }

    @PUT
    public Response update(
            @PathParam("restaurantTmplId") Integer restaurantTmplId,
            @Valid @ConvertGroup(to = OnUpdate.class) RestaurantAddressDTO req
    ) {
        service.updateAddress(req);
        return Response.noContent().build();
    }

}
