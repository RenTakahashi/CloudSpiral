package memoworld.rest;

import memoworld.entities.Account;
import memoworld.entities.Like;
import memoworld.entities.ErrorMessage;
import memoworld.model.AccessTokenModel;
import memoworld.model.LikeModel;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;



@Path("likes")
public class LikesRest {
    private static final String AUTHENTICATION_SCHEME = "Bearer";

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postLike(@Context HttpHeaders headers, Like like) {
        // 投稿者情報の取得
        String authorizationHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null &&
                authorizationHeader.toLowerCase().startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ")){
            String token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();
            AccessTokenModel accessTokenModel = new AccessTokenModel();
            Account account = accessTokenModel.getAccount(token);
            like.setAuthor(account.getName());
        }
       //TODO taraveloguesidの旅行記が存在するかの判定を行う
       //なければ404を返す
       
        try (LikeModel model = new LikeModel()) {
            like.getTraveloguesid();
        	model.register(like);
            return Response.status(201)
                    .entity(like)
                    .build();
    	}
    	
    	
    }

    @POST
    @Path("{travelogue_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response postLike(@Context HttpHeaders headers,
                             @PathParam("travelogue_id")String idString) {
        Like like = new Like(toInteger(idString));
        // 投稿者情報の取得
        String authorizationHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null &&
                authorizationHeader.toLowerCase().startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ")){
            String token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();
            AccessTokenModel accessTokenModel = new AccessTokenModel();
            Account account = accessTokenModel.getAccount(token);
            like.setAuthor(account.getName());
        }
        //TODO taraveloguesidの旅行記が存在するかの判定を行う
        //なければ404を返す

        try (LikeModel model = new LikeModel()) {
            model.register(like);
            return Response.status(201)
                    .entity(like)
                    .build();
        }
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLikes() {
		try (LikeModel model = new LikeModel()) {
			return Response.ok(model.getLikes())
					.build();
		}
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{traveloguesid}")
    public Response getLike(
    		@PathParam("traveloguesid")String idString) {
		try (LikeModel model = new LikeModel()) {
			int traveloguesid = toInteger(idString);
			//TODO traveloguesidが旅行記のコレクションに対して存在するかの確認を行う
			return Response.ok(model.getFindeq(traveloguesid))
					.build();
		}
    }
    
    

    public Response errorMessage(int statusCode,
                                 String message) {
        return Response.status(statusCode)
                .entity(new ErrorMessage(message))
                .build();

    }

    private int toInteger(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
