package org.delicias.business.resource;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.ConvertGroup;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.delicias.business.dto.BusinessCategoryReqDTO;
import org.delicias.business.service.BusinessCategoryService;
import org.delicias.common.validation.OnCreate;
import org.delicias.common.validation.OnUpdate;

@Path("/api/business")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
public class BusinessCategoryResource {


    @Inject
    BusinessCategoryService service;

    @POST
    public Response create(
            @Valid @ConvertGroup(to = OnCreate.class)
            BusinessCategoryReqDTO req
    ) {

        service.create(req);

        return Response.status(Response.Status.CREATED)
                .build();
    }

    @PUT
    public Response update(
            @Valid @ConvertGroup(to = OnUpdate.class)
            BusinessCategoryReqDTO req
    ) {

        service.update(req);

        return Response.status(Response.Status.OK)
                .build();
    }

    @GET
    @Path("/{id}")
    public Response findById(
            @PathParam("id") Integer id
    ) {

        return Response.ok(
                service.findById(id)
        ).build();

    }

    @DELETE
    @Path("/{id}")
    public Response deleteById(
            @PathParam("id") Integer id) {

        service.deleteById(id);
        return Response.noContent().build();

    }

    @GET
    public Response loadListByZoneBusinessCateg(
            @NotNull @QueryParam("zoneBusinessCategId") Integer zoneBusinessCategId,
            @QueryParam("name") String name,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size
    ) {

        return Response.ok(
                service.listByZoneBusinessCateg(zoneBusinessCategId, name, page, size)
        ).build();

    }
}
