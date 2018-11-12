package memoworld.rest;

import memoworld.entities.Like;
import memoworld.entities.ErrorMessage;
import memoworld.model.LikeModel;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("likes")
public class LikesRest {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response postLike(
            @FormParam("tid") String tid) {
        if (tid == null || tid.trim().equals(""))
            return errorMessage(404, "Not found");
        
        try (LikeModel model = new LikeModel()) {
            Like like = model.register(new Like(tid));
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
    @Path("{lid}")
    public Response getLike(
            @PathParam("lid") String idString) {
        try (LikeModel model = new LikeModel()) {
            int lid = toInteger(idString);
            if (lid <= 0)
                return errorMessage(400, "Bad request");
            Like like = model.findById(lid);
            if (like == null)
                return errorMessage(404, "Not found");
            return Response.status(200)
                    .entity(like)
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