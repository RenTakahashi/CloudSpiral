package memoworld.rest;

import memoworld.entities.Travelogue;
import memoworld.entities.Travelogues;
import memoworld.entities.ErrorMessage;
import memoworld.model.LikeModel;
import memoworld.model.PhotoModel;
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
			Travelogue travelogue = new Travelogue();
			LikeModel likemodel = new LikeModel();
			Travelogues travelogues = new Travelogues();
			PhotoModel photomodel = new PhotoModel();
			
			int LastId = (int)model.Collcount();
			int LastLid = (int)likemodel.Collcount();
			int LastPid = (int)photomodel.Collcount();
			
			for(int i = 1; i <=  LastId ; i++) {
					travelogue = model.findById(i);
				for(int j = 1; j <= LastLid; j++) {
					travelogue.likes.add(likemodel.findByLid(j));
				}
				for(int k = 1; k <= LastPid ; k++) {
					travelogue.photos.add(photomodel.findById(k));
				}
				travelogues.travelogues.add(travelogue);	
			}
				photomodel.close();
				likemodel.close();
				return Response.status(201)
						.entity(travelogues)
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
			LikeModel likemodel = new LikeModel();
			PhotoModel photomodel = new PhotoModel();
			
			int LastLid = (int)likemodel.Collcount();
			int LastPid = (int)photomodel.Collcount();
			
			for(int i = 1; i <= LastLid; i++) {
				travelogue.likes.add(likemodel.findByLid(i));
			}
			for(int j = 1; j <= LastPid; j++) {
				travelogue.photos.add(photomodel.findById(j));
			}
			likemodel.close();
			photomodel.close();
			if(travelogue == null)
				return errorMessage(404, "Not found");
			return Response.status(201)
					.entity(travelogue)
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