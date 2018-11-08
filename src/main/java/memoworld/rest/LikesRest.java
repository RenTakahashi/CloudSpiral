package memoworld.rest;

import memoworld.entities.Like;
import memoworld.entities.ErrorMessage;
import memoworld.model.LikeModel;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/likes")
public class LikesRest {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response postLike(
            @FormParam("author") String author) {
        if (author == null || author.trim().equals(""))
            return errorMessage(400, "empty body");
        if (author.length() > 80)
            return errorMessage(400, "too long body");
        try (LikeModel model = new LikeModel()) {
            Like like = model.register(new Like(author));
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
    @Path("{id}")
    public Response getLike(
            @PathParam("id") String idString) {
        try (LikeModel model = new LikeModel()) {
            int id = toInteger(idString);
            if (id <= 0)
                return errorMessage(400, "Bad request");
            Like like = model.findById(id);
            if (like == null)
                return errorMessage(404, "Not found");
            return Response.status(200)
                    .entity(like)
                    .build();

        }
    }
    
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response deleteLikes(
            @PathParam("id") String idString) {
        try (LikeModel model = new LikeModel()) {
            int id = toInteger(idString);
            if (id <= 0)
                return errorMessage(400, "Bad request");
            if (!model.deleteLikes(id))
                return errorMessage(400, "Bad request");
            return Response.status(200)
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