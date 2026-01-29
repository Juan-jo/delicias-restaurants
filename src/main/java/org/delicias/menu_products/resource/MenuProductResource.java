package org.delicias.menu_products.resource;


import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.delicias.common.validation.OnCreate;
import org.delicias.menu_products.dto.CreateMenuProductDTO;
import org.delicias.menu_products.service.MenuProductService;

@Path("/api/restaurants/{restaurantId}/menus/{menuId}/products")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MenuProductResource {

    @Inject
    MenuProductService service;

    @POST
    public Response create(
            @PathParam("restaurantId") Integer restaurantId,
            @PathParam("menuId") Integer menuId,
            @Valid @ConvertGroup(to = OnCreate.class) CreateMenuProductDTO req
    ) {

        service.create(menuId, req);
        return Response.status(Response.Status.CREATED).build();

    }

    @DELETE
    @Path("/{menuProductId}")
    public Response delete(
            @PathParam("restaurantId") Integer restaurantId,
            @PathParam("menuProductId") Integer menuProductId
    ) {

        service.deleteById(menuProductId);

        return Response.noContent().build();
    }
}
