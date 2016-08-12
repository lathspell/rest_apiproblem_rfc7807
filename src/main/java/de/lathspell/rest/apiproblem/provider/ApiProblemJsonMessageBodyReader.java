package de.lathspell.rest.apiproblem.provider;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import de.lathspell.rest.apiproblem.ApiProblem;

import static de.lathspell.rest.apiproblem.exceptions.ApiProblemException.APPLICATION_PROBLEM_JSON;

@Provider
@Consumes(APPLICATION_PROBLEM_JSON)
@Slf4j
public class ApiProblemJsonMessageBodyReader implements MessageBodyReader<ApiProblem> {

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public ApiProblem readFrom(Class<ApiProblem> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        log.debug("readFrom");
        Map<String, Object> m = new ObjectMapper().readValue(entityStream, new TypeReference<Map<String, Object>>() {
        });
        ApiProblem ap = new ApiProblem();
        for (String k : m.keySet()) {
            switch (k) {
                case "problemType":
                    ap.setProblemType((String) m.get(k));
                    break;
                case "title":
                    ap.setTitle((String) m.get(k));
                    break;
                case "httpStatus":
                    ap.setHttpStatus(Integer.valueOf((String) m.get(k)));
                    break;
                case "detail":
                    ap.setDetail((String) m.get(k));
                    break;
                case "problemInstance":
                    ap.setProblemInstance((String) m.get(k));
                    break;
                default:
                    ap.getExtras().put(k, (String) m.get(k));
            }
        }

        return ap;
    }

}
