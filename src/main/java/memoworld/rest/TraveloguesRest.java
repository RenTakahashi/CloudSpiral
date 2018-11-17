package memoworld.rest;

import memoworld.entities.Travelogue;
import memoworld.entities.ErrorMessage;
import memoworld.model.TravelogueModel;



import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/travelogues")
public class TraveloguesRest {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postTravelogue(Travelogue travelogue){
        
        try (TravelogueModel model = new TravelogueModel()) {
        	travelogue.getTitle();
        	travelogue.getDate();
        	travelogue.getPhotos_id();
        	Travelogue result =	model.register(travelogue);
        	result.setPhotos_id(null);
        	return Response.status(201)
                    .entity(result)
                    .build();
        }
    }
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTraveloges() {
		try (TravelogueModel model = new TravelogueModel()) {
			return Response.ok(model.getTravelogues())
					.build();
		}
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response getTravelogue(
    		@PathParam("id")String idString) {
		try (TravelogueModel model = new TravelogueModel()) {
			int id = toInteger(idString);
			Travelogue travelogue = model.findById(id);
			if(travelogue == null)
				return errorMessage(404, "Not found");
			return Response.ok(model.findById(id))
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