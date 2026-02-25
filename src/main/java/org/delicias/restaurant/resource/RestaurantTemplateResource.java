package org.delicias.restaurant.resource;

import io.quarkus.security.Authenticated;
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
import org.delicias.restaurant.dto.RestaurantTemplateSummaryDTO;
import org.delicias.restaurant.service.RestaurantTemplateService;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Authenticated
@Path("/api/restaurants")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RestaurantTemplateResource {

    @Inject
    RestaurantTemplateService service;


    // TODO For Restaurant Client API
    @GET
    @Path("/batch")
    public Response getByBatch(
            @QueryParam("ids") List<Integer> ids
    ) {

        return Response.ok(
                service.findByIds(ids)
        ).build();
    }

    // TODO For Restaurant Client API
    @GET
    @Path("/{restaurantTmplId}/latlng")
    public Response getLatLng(
            @PathParam("restaurantTmplId") Integer restaurantTmplId
    ) {

        return Response.ok(
                service.getLatLng(restaurantTmplId)
        ).build();
    }

    @POST
    public Response create(
            @Valid @ConvertGroup(to = OnCreate.class) RestaurantTemplateSummaryDTO req) {

        service.create(req);

        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    public Response update(
            @Valid @ConvertGroup(to = OnUpdate.class) RestaurantTemplateSummaryDTO req) {

        service.update(req);

        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(
            @PathParam("id") Integer id,
            @QueryParam("view") @DefaultValue("summary") String view
    ) {

        Object result = view.equalsIgnoreCase("full")
                ? service.findFullById(id)
                : service.findSummaryById(id);

        return Response.ok(result).build();
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
