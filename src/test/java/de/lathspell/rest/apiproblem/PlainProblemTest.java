package de.lathspell.rest.apiproblem;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlainProblemTest {

    @Test
    public void testProblem() {
        ApiProblem p = new ApiProblem().withHttpStatus(NOT_FOUND.getStatusCode()).withTitle("Not Found");

        assertEquals(NOT_FOUND.getStatusCode(), p.getHttpStatus().intValue());
        assertEquals("Not Found", p.getTitle());
    }

}
