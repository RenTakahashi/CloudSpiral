package memoworld.rest;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/hello")
public class HelloRest {
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response postHello(@FormParam("body") String body) {
        return Response.status(200)
                .entity(body)
                .build();
    }
}
