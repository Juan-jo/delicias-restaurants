package org.delicias.restaurant.resource;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.delicias.restaurant.service.StoreTypeService;

@Authenticated
@Path("/api/restaurants/type")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StoreTypeResource {

    @Inject
    StoreTypeService service;

    @GET
    public Response loadTypes() {

        return Response.ok(
                service.getStoreTypesList()
        ).build();
    }



}
