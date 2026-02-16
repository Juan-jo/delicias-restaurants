package org.delicias.products_recommend.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.delicias.common.validation.OnCreate;
import org.delicias.products_recommend.dto.CreateProductRecommendDTO;
import org.delicias.products_recommend.service.ProductRecommendService;

import java.util.Map;


@Path("/api/restaurants/{restaurantId}/recommended")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductRecommendResource {

    @Inject
    ProductRecommendService service;

    @POST
    public Response create(
            @PathParam("restaurantId") Integer restaurantId,
            @Valid @ConvertGroup(to = OnCreate.class) CreateProductRecommendDTO req) {

        service.create(restaurantId, req);

        return Response.status(Response.Status.CREATED).build();
    }


    @DELETE
    @Path("/{productRecommendedId}")
    public Response delete(
            @PathParam("restaurantId") Integer restaurantId,
            @PathParam("productRecommendedId") Integer productRecommendedId) {

        service.delete(productRecommendedId);

        return Response.noContent().build();
    }

    @GET
    @Path("/{productRecommendedId}")
    public Response getById(
            @PathParam("restaurantId") Integer restaurantId,
            @PathParam("productRecommendedId") Integer productRecommendedId) {

        return Response.ok(
                service.findById(productRecommendedId)
        ).build();
    }

    @GET
    public Response recommendByRestaurantTmpl(
            @PathParam("restaurantId") Integer restaurantId,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("5") int size
    ) {

        return Response.ok(
                service.findByRestaurant(restaurantId, page , size)
        ).build();
    }

    @PATCH
    @Path("/{productRecommendedId}")
    public Response patch(
            @PathParam("restaurantId") Integer restaurantId,
            @PathParam("productRecommendedId") Integer productRecommendedId,
            Map<String, Object> data
    ) {

        service.patch(productRecommendedId, data);
        return Response.ok().build();
    }
}
