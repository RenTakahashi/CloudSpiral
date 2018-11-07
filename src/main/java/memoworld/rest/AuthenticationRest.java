package memoworld.rest;

import memoworld.entities.AccessToken;
import memoworld.entities.Account;
import memoworld.entities.Authentication;
import memoworld.model.AccessTokenModel;

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
        // 仮実装 ここから
        Account account = new Account();
        account.setUserId(authentication.getUserId());
        account.setPassword(authentication.getPassword());
        account.setUserName(authentication.getUserId());
        // 仮実装 ここまで

        AccessTokenModel model = new AccessTokenModel();
        AccessToken token = model.register(account);

        return Response.ok(token).build();
    }
}
