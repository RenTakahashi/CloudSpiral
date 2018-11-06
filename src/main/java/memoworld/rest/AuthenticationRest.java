package memoworld.rest;

import memoworld.entities.Authentication;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/authentication")
public class AuthenticationRest {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postAuthentication(Authentication authentication) {
        // TODO: アカウントDBにuser_idとpasswordの組が登録されているか?　なければ401


        // TODO: AccessTokenModelにTokenを要求


        return Response.ok().build();
    }
}
