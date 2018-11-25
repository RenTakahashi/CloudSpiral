package memoworld.rest;

import memoworld.entities.AccessToken;
import memoworld.entities.Account;
import memoworld.entities.Authentication;
import memoworld.entities.ErrorMessage;
import memoworld.model.AccessTokenModel;
import memoworld.model.AccountModel;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

@Path("/authentication")
public class AuthenticationRest {
    @POST
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response postAuthentication(Authentication authentication) {
        AccountModel accountModel = new AccountModel();
        Account account = accountModel.findByUserIdAndPassword(authentication.getUserId(), authentication.getPassword());
        if (account == null) {
            return Response.status(UNAUTHORIZED).entity(new ErrorMessage("user_id or password is wrong.")).build();
        }

        AccessTokenModel model = new AccessTokenModel();
        AccessToken token = model.register(account);

        return Response.ok(token).build();
    }
}
