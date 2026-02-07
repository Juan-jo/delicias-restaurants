package org.delicias.config;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.hibernate.exception.ConstraintViolationException;

@Provider
public class HibernateConstraintMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {

        String detail = "El registro ya existe en la base de datos.";

        String constraintName = exception.getConstraintName();

        ErrorMessage error = new ErrorMessage(
                "Conflicto de datos",
                detail,
                409 // Conflict
        );

        return Response.status(Response.Status.CONFLICT)
                .entity(error)
                .build();
    }

    public static class ErrorMessage {
        public String title;
        public String message;
        public int status;

        public ErrorMessage(String title, String message, int status) {
            this.title = title;
            this.message = message;
            this.status = status;
        }
    }
}