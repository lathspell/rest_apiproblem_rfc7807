package de.lathspell.rest.apiproblem.server;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

import lombok.extern.slf4j.Slf4j;

import de.lathspell.rest.apiproblem.exceptions.ApiProblemException;

/**
 * This class catches server side exceptions and generates RFC 7807 "Problem" responses.
 */
@Provider
@Slf4j
public class RestServerExceptionMapper implements ExceptionMapper<Exception> {

    public RestServerExceptionMapper() {
        log.debug("ctor");
    }

    @Override
    public Response toResponse(Exception e) {
        log.debug("toResponse");
        if (e instanceof WebApplicationException) {
            Response.StatusType st = ((WebApplicationException) e).getResponse().getStatusInfo();
            log.info("Caught WebApplicationException in REST request: {} {}", st.getStatusCode(), st.getReasonPhrase());
            log.debug("Stack trace:", e);
            return new ApiProblemException((WebApplicationException) e).getResponse();
        } else {
            log.warn("Caught Exception in REST request: " + e.getMessage(), e);
            return new ApiProblemException(INTERNAL_SERVER_ERROR, e).getResponse();
        }
    }
}
