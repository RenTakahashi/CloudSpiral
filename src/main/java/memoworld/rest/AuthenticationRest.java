package memoworld.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import memoworld.entities.Authentication;

//import lama.entities.Like;
//import lama.model.LikeModel;

@Path("/authentication")
public class AuthenticationRest {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postAuthentication(Authentication authentication) {
    	return Response.ok().build();
    }
}
