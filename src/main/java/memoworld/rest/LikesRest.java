package memoworld.rest;


import memoworld.entities.Like;
import memoworld.model.LikeModel;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/likes")
public class LikesRest {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response postLikes() {
    	try (LikeModel model = new LikeModel()) {
            Like like = new Like();
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
            return Response.status(200)
                    .entity(model.list())
                    .build();
    	}
    }
}