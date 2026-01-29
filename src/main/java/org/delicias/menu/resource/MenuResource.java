package org.delicias.menu.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.delicias.common.validation.OnCreate;
import org.delicias.common.validation.OnUpdate;
import org.delicias.menu.dto.MenuDTO;
import org.delicias.menu.service.MenuService;

@Path("/api/restaurants/{restaurantId}/menus")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MenuResource {

    @Inject
    MenuService service;


    @POST
    public Response create(
            @PathParam("restaurantId") Integer restaurantId,
            @Valid @ConvertGroup(to = OnCreate.class) MenuDTO req
    ) {

        service.create(restaurantId, req);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    public Response update(
            @PathParam("restaurantId") Integer restaurantId,
            @Valid @ConvertGroup(to = OnUpdate.class) MenuDTO req
    ) {

        service.update(req);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    public Response findMenus(
            @PathParam("restaurantId") Integer restaurantId
    ) {

        return Response.ok(
                service.findByRestaurant(restaurantId)
        ).build();
    }


    @DELETE
    @Path("/{menuId}")
    public Response delete(
            @PathParam("restaurantId") Integer restaurantId,
            @PathParam("menuId") Integer menuId
    ) {

        service.deleteById(menuId);

        return Response.noContent().build();
    }

}
