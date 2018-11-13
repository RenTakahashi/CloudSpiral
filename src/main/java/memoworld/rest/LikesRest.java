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
            @FormParam("traveloguesid") int traveloguesid) {
        //if (tid == null || tid.trim().equals(""))
         //   return errorMessage(400, "Not found");
       
        try (LikeModel model = new LikeModel()) {
            Like like = model.register(new Like(traveloguesid));
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
            @PathParam("traveloguesid") String idString) {
        try (LikeModel model = new LikeModel()) {
            int traveloguesid = toInteger(idString);
            if (traveloguesid <= 0)
                return errorMessage(400, "Bad request");
            Like like = model.findById(traveloguesid);
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