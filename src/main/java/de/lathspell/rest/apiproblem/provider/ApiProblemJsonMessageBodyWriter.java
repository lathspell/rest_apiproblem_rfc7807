package de.lathspell.rest.apiproblem.provider;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import de.lathspell.rest.apiproblem.ApiProblem;

import static de.lathspell.rest.apiproblem.exceptions.ApiProblemException.APPLICATION_PROBLEM_JSON;

@Provider
@Produces(APPLICATION_PROBLEM_JSON)
@Slf4j
public class ApiProblemJsonMessageBodyWriter implements MessageBodyWriter<ApiProblem> {

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public long getSize(ApiProblem t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(ApiProblem ap, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        log.debug("writeTo");
        Map<String, String> buffer = new HashMap<>();
        buffer.put("problemType", ap.getProblemType());
        buffer.put("title", ap.getTitle());
        if (ap.getHttpStatus() == null) {
            buffer.put("httpStatus", String.valueOf(INTERNAL_SERVER_ERROR.getStatusCode()));
        } else {
            buffer.put("httpStatus", ap.getHttpStatus().toString());
        }
        if (ap.getDetail() != null) {
            buffer.put("detail", ap.getDetail());
        }
        if (ap.getProblemInstance() != null) {
            buffer.put("problemInstance", ap.getProblemInstance());
        }
        if (ap.getExtras() != null) {
            for (String k : ap.getExtras().keySet()) {
                buffer.put(k, ap.getExtras().get(k));
            }
        }

        String json = new ObjectMapper().writeValueAsString(buffer);
        entityStream.write(json.getBytes());
    }

}
