package org.delicias.restaurant.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.ConvertGroup;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.delicias.common.validation.OnCreate;
import org.delicias.common.validation.OnFilter;
import org.delicias.common.validation.OnUpdate;
import org.delicias.restaurant.dto.RestaurantFilterReqDTO;
import org.delicias.restaurant.dto.RestaurantTemplateDTO;
import org.delicias.restaurant.service.RestaurantTemplateService;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.IOException;
import java.util.Map;

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

    @POST
    @Path("/filter")
    public Response filterSearch(
            @Valid @ConvertGroup(to = OnFilter.class) RestaurantFilterReqDTO req) {

        return Response.ok(
                service.filterSearch(req)
        ).build();
    }

    @PUT
    @Path("/picture")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateLogo(
            @NotNull @FormParam("restaurantTmplId") Integer restaurantTmplId,
            @NotNull @FormParam("file") FileUpload file,
            @NotNull @FormParam("typeImage")TYPE_IMAGE typeImage
    ) throws IOException {

        Map<String, String> response = null;

        if (typeImage.equals(TYPE_IMAGE.LOGO)) {
            response = service.uploadLogo(restaurantTmplId, file);
        } else if (typeImage.equals(TYPE_IMAGE.COVER)) {
            response = service.uploadCover(restaurantTmplId, file);
        }
        return Response.ok(response).build();
    }


    public enum TYPE_IMAGE {
        COVER,
        LOGO
    }

}
