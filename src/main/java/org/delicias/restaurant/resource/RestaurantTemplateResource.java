package org.delicias.restaurant.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.delicias.common.validation.OnCreate;
import org.delicias.common.validation.OnUpdate;
import org.delicias.restaurant.dto.RestaurantTemplateDTO;
import org.delicias.restaurant.service.RestaurantTemplateService;

@Path("/api/restaurants")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RestaurantTemplateResource {

    @Inject
    RestaurantTemplateService service;

    @POST
    public Response create(
            @Valid @ConvertGroup(to = OnCreate.class) RestaurantTemplateDTO req) {

        service.create(req);

        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    public Response update(
            @Valid @ConvertGroup(to = OnUpdate.class) RestaurantTemplateDTO req) {

        service.update(req);

        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Integer id) {

        var response = service.findById(id);
        return Response.ok(response).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Integer id) {

        service.deleteById(id);
        return Response.noContent().build();
    }


}
